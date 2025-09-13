package com.example.Kiraflow;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests that follow the 10-step flow:
 * 1. Register user1
 * 2. Login user1
 * 3. Create org & project
 * 4. Create board & column
 * 5. Register user2 & login (non-member)
 * 6. Non-member cannot create task (403)
 * 7. Member creates task, get by id, list by column
 * 8. Update task
 * 9. Label attach/list/remove
 * 10. Comment create/list/update/delete + move task
 *
 * Adjust endpoint paths / JSON field names if your app differs.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class KiraflowApplicationTests {

    @LocalServerPort int port;

    @Autowired TestRestTemplate rest;

    private final ObjectMapper mapper = new ObjectMapper();

    // shared state across ordered tests
    private static String token1;
    private static String token2;
    private static UUID user1Id;
    private static UUID orgId;
    private static UUID projectId;
    private static UUID boardId;
    private static UUID columnId;
    private static UUID taskId;
    private static UUID labelId;
    private static UUID commentId;
    private static UUID targetColumnId;

    private String base() { return "http://localhost:" + port; }

    // helper to POST JSON and return ResponseEntity<String>
    private ResponseEntity<String> postJson(String url, String json, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (authToken != null) headers.setBearerAuth(authToken);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        return rest.exchange(url, HttpMethod.POST, entity, String.class);
    }

    private ResponseEntity<String> putJson(String url, String json, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (authToken != null) headers.setBearerAuth(authToken);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        return rest.exchange(url, HttpMethod.PUT, entity, String.class);
    }

    private ResponseEntity<String> get(String url, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        if (authToken != null) headers.setBearerAuth(authToken);
        HttpEntity<Void> e = new HttpEntity<>(headers);
        return rest.exchange(url, HttpMethod.GET, e, String.class);
    }

    private ResponseEntity<String> delete(String url, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        if (authToken != null) headers.setBearerAuth(authToken);
        HttpEntity<Void> e = new HttpEntity<>(headers);
        return rest.exchange(url, HttpMethod.DELETE, e, String.class);
    }

    @Test
    @Order(1)
    void registerUser1() throws Exception {
        String payload = mapper.writeValueAsString(Map.of(
                "name", "testuser1",
                "email", "test10@example.com",
                "password", "Pass1234"
        ));
        ResponseEntity<String> r = postJson(base() + "/api/auth/register", payload, null);
        assertTrue(r.getStatusCode().is2xxSuccessful(), "register should return 2xx, got: " + r.getStatusCode());
        JsonNode body = mapper.readTree(r.getBody());
        // try common fields for id
        if (body.has("id")) user1Id = UUID.fromString(body.get("id").asText());
        else if (body.has("userId")) user1Id = UUID.fromString(body.get("userId").asText());
        // otherwise leave null
        // no strict assert for id as API variations exist
        token1 = null;
    }

    @Test
    @Order(2)
    void loginUser1() throws Exception {
        String payload = mapper.writeValueAsString(Map.of(
                "usernameOrEmail", "test10@example.com",
                "password", "Pass1234"
        ));
        ResponseEntity<String> r = postJson(base() + "/api/auth/login", payload, null);
        assertTrue(r.getStatusCode().is2xxSuccessful(), "login should return 2xx");
        JsonNode body = mapper.readTree(r.getBody());
        if (body.has("token")) token1 = body.get("token").asText();
        else if (body.has("accessToken")) token1 = body.get("accessToken").asText();
        assertNotNull(token1, "token must be present");
    }

    @Test
    @Order(3)
    void createOrgAndProject() throws Exception {
        // create org
        String orgPayload = mapper.writeValueAsString(Map.of("name", "TestOrg"));
        ResponseEntity<String> r1 = postJson(base() + "/api/organizations", orgPayload, token1);
        assertTrue(r1.getStatusCode().is2xxSuccessful(), "create org should be 2xx");
        JsonNode b1 = mapper.readTree(r1.getBody());
        assertTrue(b1.has("id") || b1.has("orgId"));
        orgId = UUID.fromString(b1.has("id") ? b1.get("id").asText() : b1.get("orgId").asText());

        // create project
        String projPayload = mapper.writeValueAsString(Map.of("orgId", orgId.toString(), "name", "TestProject"));
        ResponseEntity<String> r2 = postJson(base() + "/api/projects", projPayload, token1);
        assertTrue(r2.getStatusCode().is2xxSuccessful(), "create project should be 2xx");
        JsonNode b2 = mapper.readTree(r2.getBody());
        projectId = UUID.fromString(b2.has("id") ? b2.get("id").asText() : b2.get("projectId").asText());
    }

    @Test
    @Order(4)
    void createBoardAndColumn() throws Exception {
        String boardPayload = mapper.writeValueAsString(Map.of("projectId", projectId.toString(), "name", "Board A"));
        ResponseEntity<String> r1 = postJson(base() + "/api/boards", boardPayload, token1);
        assertTrue(r1.getStatusCode().is2xxSuccessful(), "create board should be 2xx");
        JsonNode b = mapper.readTree(r1.getBody());
        boardId = UUID.fromString(b.get("id").asText());

        String colPayload = mapper.writeValueAsString(Map.of("boardId", boardId.toString(), "title", "To Do", "positionIndex", 0));
        ResponseEntity<String> r2 = postJson(base() + "/api/columns", colPayload, token1);
        assertTrue(r2.getStatusCode().is2xxSuccessful(), "create column should be 2xx");
        JsonNode c = mapper.readTree(r2.getBody());
        columnId = UUID.fromString(c.get("id").asText());
    }

    @Test
    @Order(5)
    void registerAndLoginUser2() throws Exception {
        String payload = mapper.writeValueAsString(Map.of(
                "name", "testuser2",
                "email", "test20@example.com",
                "password", "Pass1234"
        ));
        ResponseEntity<String> r = postJson(base() + "/api/auth/register", payload, null);
        assertTrue(r.getStatusCode().is2xxSuccessful(), "register user2 should be 2xx");

        String login = mapper.writeValueAsString(Map.of("usernameOrEmail", "test20@example.com", "password", "Pass1234"));
        ResponseEntity<String> r2 = postJson(base() + "/api/auth/login", login, null);
        assertTrue(r2.getStatusCode().is2xxSuccessful(), "login user2 should be 2xx");
        JsonNode body = mapper.readTree(r2.getBody());
        token2 = body.has("token") ? body.get("token").asText() : body.get("accessToken").asText();
        assertNotNull(token2);
    }

    @Test
    @Order(6)
    void nonMemberCannotCreateTask() throws Exception {
        String taskPayload = mapper.writeValueAsString(Map.of("columnId", columnId.toString(), "type", "TASK", "title", "Try create as non-member"));
        ResponseEntity<String> r = postJson(base() + "/api/tasks", taskPayload, token2);
        assertEquals(HttpStatus.FORBIDDEN, r.getStatusCode(), "non-member should be forbidden (403)");
    }

    @Test
    @Order(7)
    void memberCreatesTaskAndFetches() throws Exception {
        String taskPayload = mapper.writeValueAsString(Map.of("columnId", columnId.toString(), "type", "TASK", "title", "First task", "description", "desc"));
        ResponseEntity<String> r = postJson(base() + "/api/tasks", taskPayload, token1);
        assertTrue(r.getStatusCode().is2xxSuccessful(), "create task should be 2xx");
        JsonNode body = mapper.readTree(r.getBody());
        taskId = UUID.fromString(body.get("id").asText());

        // get by id
        ResponseEntity<String> r2 = get(base() + "/api/tasks/" + taskId, token1);
        assertTrue(r2.getStatusCode().is2xxSuccessful());
        JsonNode b2 = mapper.readTree(r2.getBody());
        assertEquals("First task", b2.get("title").asText());

        // list by column
        ResponseEntity<String> r3 = get(base() + "/api/tasks/column/" + columnId, token1);
        assertTrue(r3.getStatusCode().is2xxSuccessful());
        JsonNode list = mapper.readTree(r3.getBody());
        assertTrue(list.isArray());
        boolean found = false;
        for (JsonNode n : list) if (n.has("id") && n.get("id").asText().equals(taskId.toString())) found = true;
        assertTrue(found, "created task should appear in column listing");
    }

    @Test
    @Order(8)
    void updateTask() throws Exception {
        String updatePayload = mapper.writeValueAsString(Map.of(
                "columnId", null,
                "epicId", null,
                "assigneeId", null,
                "type", "TASK",
                "title", "Updated title",
                "description", "updated",
                "storyPoints", 5,
                "status", "IN_PROGRESS"
        ));
        ResponseEntity<String> r = putJson(base() + "/api/tasks/" + taskId, updatePayload, token1);
        assertTrue(r.getStatusCode().is2xxSuccessful());
        JsonNode body = mapper.readTree(r.getBody());
        assertEquals("Updated title", body.get("title").asText());
        assertEquals(5, body.get("storyPoints").asInt());
        assertEquals("IN_PROGRESS", body.get("status").asText());
    }

    @Test
    @Order(9)
    void labelsAttachListRemove() throws Exception {
        // create label
        String labelPayload = mapper.writeValueAsString(Map.of("orgId", orgId.toString(), "name", "bug", "color", "#ff0000"));
        ResponseEntity<String> r = postJson(base() + "/api/labels", labelPayload, token1);
        assertTrue(r.getStatusCode().is2xxSuccessful());
        JsonNode body = mapper.readTree(r.getBody());
        labelId = UUID.fromString(body.get("id").asText());

        // attach label
        ResponseEntity<String> rAttach = postJson(base() + "/api/task-entities/" + taskId + "/labels?labelId=" + labelId, "", token1);
        assertTrue(rAttach.getStatusCode().is2xxSuccessful());

        // list labels
        ResponseEntity<String> rList = get(base() + "/api/task-entities/" + taskId + "/labels", token1);
        assertTrue(rList.getStatusCode().is2xxSuccessful());
        JsonNode arr = mapper.readTree(rList.getBody());
        boolean found = false;
        for (JsonNode n : arr) if (n.has("id") && n.get("id").asText().equals(labelId.toString())) found = true;
        assertTrue(found);

        // remove label
        ResponseEntity<String> rRem = delete(base() + "/api/task-entities/" + taskId + "/labels/" + labelId, token1);
        assertTrue(rRem.getStatusCode().is2xxSuccessful());
    }

    @Test
    @Order(10)
    void commentsCreateListUpdateDeleteAndMoveTask() throws Exception {
        // create comment
        String commentPayload = mapper.writeValueAsString(Map.of("taskId", taskId.toString(), "content", "Nice!"));
        ResponseEntity<String> r = postJson(base() + "/api/comments", commentPayload, token1);
        assertTrue(r.getStatusCode().is2xxSuccessful());
        JsonNode cbody = mapper.readTree(r.getBody());
        commentId = UUID.fromString(cbody.get("id").asText());

        // list comments
        ResponseEntity<String> rList = get(base() + "/api/comments/task/" + taskId, token1);
        assertTrue(rList.getStatusCode().is2xxSuccessful());
        JsonNode arr = mapper.readTree(rList.getBody());
        boolean found = false;
        for (JsonNode n : arr) if (n.has("id") && n.get("id").asText().equals(commentId.toString())) found = true;
        assertTrue(found);

        // update comment
        String upd = mapper.writeValueAsString(Map.of("content", "Edited comment"));
        ResponseEntity<String> rUpd = putJson(base() + "/api/comments/" + commentId, upd, token1);
        assertTrue(rUpd.getStatusCode().is2xxSuccessful());
        JsonNode updated = mapper.readTree(rUpd.getBody());
        assertEquals("Edited comment", updated.get("content").asText());

        // delete comment
        ResponseEntity<String> rDel = delete(base() + "/api/comments/" + commentId, token1);
        assertTrue(rDel.getStatusCode().is2xxSuccessful());

        // create another column (target) to move task
        String colPayload = mapper.writeValueAsString(Map.of("boardId", boardId.toString(), "title", "In Progress", "positionIndex", 1));
        ResponseEntity<String> rCol = postJson(base() + "/api/columns", colPayload, token1);
        assertTrue(rCol.getStatusCode().is2xxSuccessful());
        JsonNode colBody = mapper.readTree(rCol.getBody());
        targetColumnId = UUID.fromString(colBody.get("id").asText());

        // move task
        ResponseEntity<String> rMove = postJson(base() + "/api/tasks/" + taskId + "/move?targetColumnId=" + targetColumnId + "&positionIndex=1", "", token1);
        assertTrue(rMove.getStatusCode().is2xxSuccessful());
        JsonNode moved = mapper.readTree(rMove.getBody());
        assertEquals(targetColumnId.toString(), moved.get("columnId").asText());
    }
}

