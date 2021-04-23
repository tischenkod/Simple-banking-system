package banking.menu;

@FunctionalInterface
public interface EventHandler {
    void handle(MenuItem sender);
}
