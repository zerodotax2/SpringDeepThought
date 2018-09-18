package ru.projects.prog_ja.services.files.fileServices;

public class FileAndDirectory {

    private String filePath;
    private String directoryPath;

    public FileAndDirectory() {
    }

    public FileAndDirectory(String filePath, String directoryPath) {
        this.filePath = filePath;
        this.directoryPath = directoryPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }
}
