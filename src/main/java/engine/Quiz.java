package engine;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Quiz {
    private static int idCounter = 0;
    private final int id = ++idCounter;
    @NotBlank(message = "title is mandatory")
    private final String title;
    @NotBlank(message = "text is mandatory")
    private final String text;
    @NotNull
    @Size(min = 2, message = "options.size should be >= 2")
    private final String[] options;
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private final int[] answer;

    public Quiz(String title, String text, String[] options, int[] answer) {
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

    public int[] getAnswer() {
        return answer;
    }
}
