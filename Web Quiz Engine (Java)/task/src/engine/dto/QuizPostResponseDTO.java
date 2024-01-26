package engine.dto;

public class QuizPostResponseDTO {
    private long id;
    private String title;
    private String text;
    private String[] options;

    public QuizPostResponseDTO(long id, String title, String text, String[] options) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.options = options;
    }

    public long getId() {
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
}
