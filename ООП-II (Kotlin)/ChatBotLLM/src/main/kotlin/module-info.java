module com.zabguzxcoop.chatbotllm {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires org.json;

    opens com.zabguzxcoop.chatbotllm to javafx.fxml;
    exports com.zabguzxcoop.chatbotllm;
}