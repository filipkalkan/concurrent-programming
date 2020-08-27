import javax.swing.SwingUtilities;

import chat.client.ChatWindow;
import chat.client.Twit;

public class ChatWithTwit {
    public static void main(String[] args) throws Exception {
        // create an imaginary friend to talk to, generating 100 messages
        // with (up to) 5 seconds between them
        new Twit("twit", 100, 5000).start();

        // then open the chat window
        SwingUtilities.invokeAndWait(() -> new ChatWindow().login());
    }
}
