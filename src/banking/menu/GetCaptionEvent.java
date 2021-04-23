package banking.menu;

@FunctionalInterface
public interface GetCaptionEvent {
    String handle(MenuItem sender);
}
