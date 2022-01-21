package Base.utils.restassured;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.attachment.AttachmentData;
import io.qameta.allure.attachment.AttachmentRenderer;
import io.qameta.allure.attachment.DefaultAttachmentContent;

import java.io.IOException;
import java.util.Objects;

public class RestAssuredAttachmentRenderer implements AttachmentRenderer<AttachmentData> {

    @Override
    public DefaultAttachmentContent render(final AttachmentData data) {
        if (data instanceof RequestAttachment reqData) {
            String contentType = reqData.getCurl() != null ? "text/plain" : "application/json";
            String fileExtension = reqData.getCurl() != null ? ".txt" : ".json";
            String content = null;

            if (reqData.getCurl() == null && reqData.getBody() == null) {
                Object value = null;

                if (!reqData.getHeaders().isEmpty()) {
                    value = reqData.getHeaders();
                } else if (!reqData.getCookies().isEmpty()) {
                    value = reqData.getCookies();
                }

                try {
                    content = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else if (reqData.getCurl() == null && reqData.getBody() != null) {
                try {
                    content = new ObjectMapper()
                            .writerWithDefaultPrettyPrinter()
                            .writeValueAsString(new ObjectMapper().readValue(reqData.getBody(), Object.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                content = reqData.getCurl();
            }

            return new DefaultAttachmentContent(content, contentType, fileExtension);
        } else {
            ResponseAttachment resData = (ResponseAttachment) data;
            String content = null;

            try {
                if (!resData.getHeaders().isEmpty()) {
                    content = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(resData.getHeaders());
                } else if (Objects.nonNull(resData.getBody())) {
                    String value = resData.getBody().replaceAll("<(/)?[A-Za-z]+>", "").strip();
                    content = new ObjectMapper()
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(new ObjectMapper().readValue(value, Object.class));
                }
            } catch (IOException e) {}

            return new DefaultAttachmentContent(content, "application/json", ".json");
        }
    }
}
