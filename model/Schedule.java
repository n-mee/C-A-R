package model;

public class Schedule {
    private final String day;
    private final String room;
    private final String start;
    private final String end;
    private final String subject;
    private final String section;
    private final String professor;
    
    public Schedule (            
            String day,
            String room,
            String start,
            String end,
            String subject,
            String section,
            String professor ) {
        this.day = day;
        this.room = room;
        this.start = start;
        this.end = end;
        this.subject = subject;
        this.section = section;
        this.professor = professor;
    }

    public String getDay() {
        return day;
    }

    public String getRoom() {
        return room;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getSubject() {
        return subject;
    }

    public String getSection() {
        return section;
    }

    public String getProfessor() {
        return professor;
    }
    
}
