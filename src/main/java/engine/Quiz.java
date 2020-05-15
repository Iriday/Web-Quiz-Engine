package engine;

public class Quiz {
    private final String title;
    private final String text;
    private final String[] options;

    public Quiz(String title, String text, String[] options) {
        this.title = title;
        this.text = text;
        this.options = options;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String[] getOptions() {
        return options;
    }
}
