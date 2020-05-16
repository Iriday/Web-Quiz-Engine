package engine;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Quiz {
    private static int idCounter = 0;
    private final int id = ++idCounter;
    private final String title;
    private final String text;
    private final String[] options;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private final int answer;

    public Quiz(String title, String text, String[] options, int answer) {
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;
    }

    public int getId() {
        return id;
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

    public int getAnswer() {
        return answer;
    }
}
