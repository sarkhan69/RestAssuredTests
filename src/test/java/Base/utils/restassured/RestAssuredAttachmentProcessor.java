package Base.utils.restassured;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.attachment.AttachmentContent;
import io.qameta.allure.attachment.AttachmentData;
import io.qameta.allure.attachment.AttachmentProcessor;
import io.qameta.allure.attachment.AttachmentRenderer;

import java.nio.charset.StandardCharsets;

public class RestAssuredAttachmentProcessor implements AttachmentProcessor<AttachmentData> {

    private final AllureLifecycle lifecycle;

    public RestAssuredAttachmentProcessor() {
        this(Allure.getLifecycle());
    }

    public RestAssuredAttachmentProcessor(final AllureLifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    @Override
    public void addAttachment(final AttachmentData attachmentData,
                              final AttachmentRenderer<AttachmentData> renderer) {
        final AttachmentContent content = renderer.render(attachmentData);

        lifecycle.addAttachment(
                attachmentData.getName(),
                content.getContentType(),
                content.getFileExtension(),
                (content.getContent() != null) ? content.getContent().getBytes(StandardCharsets.UTF_8) : new byte[1]);
    }
}
