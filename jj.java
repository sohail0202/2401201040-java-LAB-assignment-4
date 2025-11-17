import java.io.*;
import java.util.*;

class Student {
    Integer rollNo;
    String name;
    String email;
    String course;
    Double marks;

    public Student(Integer rollNo, String name, String email, String course, Double marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.email = email;
        this.course = course;
        this.marks = marks;
    }

    public String toCSV() {
        return rollNo + "," + name.replace(",", ";") + "," + email.replace(",", ";") + "," + course.replace(",", ";") + "," + marks;
    }

    public static Student fromCSV(String line) {
        String[] p = line.split(",", -1);
        return new Student(Integer.valueOf(p[0].trim()), p[1].trim(), p[2].trim(), p[3].trim(), Double.valueOf(p[4].trim()));
    }

    public void display() {
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name   : " + name);
        System.out.println("Email  : " + email);
        System.out.println("Course : " + course);
        System.out.println("Marks  : " + marks);
        System.out.println("Grade  : " + grade());
        System.out.println("---------------------------");
    }

    private String grade() {
        if (marks >= 90) return "A";
        if (marks >= 75) return "B";
        if (marks >= 60) return "C";
        if (marks >= 40) return "D";
        return "F";
    }
}

class FileUtil {
    private final File file;

    public FileUtil(String filename) {
        this.file = new File(filename);
    }

    public List<Student> loadAll() {
        List<Student> list = new ArrayList<>();
        if (!file.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String l;
            while ((l = br.readLine()) != null) {
                if (l.trim().isEmpty()) continue;
                try { list.add(Student.fromCSV(l)); } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
        return list;
    }

    public boolean saveAll(List<Student> students) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Student s : students) {
                bw.write(s.toCSV());
                bw.newLine();
            }
            return true;
        } catch (Exception e) { return false; }
    }
}

public class jj {
    private static final String DATA_FILE = "students.txt";
    private final Scanner sc;
    private final List<Student> students;
    private final FileUtil fileUtil;

    public jj() {
        sc = new Scanner(System.in);
        fileUtil = new FileUtil(DATA_FILE);
        students = fileUtil.loadAll();
    }

    public void startMenu() {
        while (true) {
            System.out.println("\n1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search by Name");
            System.out.println("4. Delete by Name");
            System.out.println("5. Sort by Marks");
            System.out.println("6. Save and Exit");
            System.out.print("Enter choice: ");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1": addStudent(); break;
                case "2": viewAll(); break;
                case "3": searchByName(); break;
                case "4": deleteByName(); break;
                case "5": sortByMarks(); break;
                case "6": saveAndExit(); return;
                default: System.out.println("Invalid choice");
            }
        }
    }

    private void addStudent() {
        try {
            System.out.print("Enter Roll No: ");
            Integer roll = Integer.valueOf(sc.nextLine().trim());
            for (Student s : students) {
                if (s.rollNo.equals(roll)) {
                    System.out.println("Duplicate Roll No");
                    return;
                }
            }
            System.out.print("Enter Name: ");
            String name = sc.nextLine().trim();
            System.out.print("Enter Email: ");
            String email = sc.nextLine().trim();
            System.out.print("Enter Course: ");
            String course = sc.nextLine().trim();
            System.out.print("Enter Marks: ");
            Double marks = Double.valueOf(sc.nextLine().trim());
            if (marks < 0 || marks > 100) {
                System.out.println("Marks must be 0-100");
                return;
            }
            students.add(new Student(roll, name, email, course, marks));
            System.out.println("Student Added");
        } catch (Exception e) {
            System.out.println("Invalid Input");
        }
    }

    private void viewAll() {
        if (students.isEmpty()) {
            System.out.println("No Records");
            return;
        }
        for (Student s : students) s.display();
    }

    private void searchByName() {
        System.out.print("Enter Name: ");
        String n = sc.nextLine().trim();
        boolean f = false;
        for (Student s : students) {
            if (s.name.equalsIgnoreCase(n)) {
                s.display();
                f = true;
            }
        }
        if (!f) System.out.println("Not Found");
    }

    private void deleteByName() {
        System.out.print("Enter Name: ");
        String n = sc.nextLine().trim();
        boolean removed = students.removeIf(s -> s.name.equalsIgnoreCase(n));
        System.out.println(removed ? "Deleted" : "No Match Found");
    }

    private void sortByMarks() {
        students.sort((a, b) -> Double.compare(b.marks, a.marks));
        System.out.println("Sorted by Marks");
    }

    private void saveAndExit() {
        if (fileUtil.saveAll(students)) System.out.println("Saved");
        else System.out.println("Save Failed");
        System.out.println("Exit");
        sc.close();
    }

    public static void main(String[] args) {
        new jj().startMenu();
    }
}
