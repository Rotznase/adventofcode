package de.streubel.aoc22;

import de.streubel.AdventOfCodeRunner;

import java.util.ArrayList;
import java.util.List;

public class Day07 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        Directory root = null;
        Directory current = null;
        boolean ls = false;
        for (String line : stringInput) {
            if (line.indexOf("$") == 0) {
                final String[] split = line.split(" +");
                switch (split[1]) {
                    case "cd":
                        ls = false;
                        if (split[2].equals("/")) {
                            root = new Directory(split[2], null);
                            current = root;
                        } else if (split[2].equals("..")) {
                            current = current.getParent();
                        } else {
                            current = current.addDirectory(new Directory(split[2], current));
                        }

                        break;
                    case "ls":
                        ls = true;
                        break;
                }
            } else {
                if (ls) {
                    final String[] split = line.split(" +");
                    if (!split[0].equals("dir")) {
                        current.addFile(Long.parseLong(split[0]), split[1]);
                    }
                }
            }
        }

        assert root != null;

        // Part 1
        long maxSize = 100000;
        long resultPart1 = root.flatten().stream()
                .map(Directory::getSize)
                .filter(size -> size <= maxSize)
                .reduce(Long::sum)
                .get();

        System.out.println("Result Part 1 (1367870): " + resultPart1);


        // Part 2
        long totalDiskSpace = 70000000L;
        long neededUnusedSpace = 30000000L;
        long minSizeToDelete = neededUnusedSpace - (totalDiskSpace - root.getSize());

        long resultPart2 = root.flatten().stream()
                .map(Directory::getSize)
                .filter(size -> size >= minSizeToDelete)
                .reduce(Long::min)
                .get();

        System.out.println("Result Part 2 (549173): " + resultPart2);

    }

    private static class Directory {
        private final String name;
        private Directory parent;
        private final List<Directory> directories;
        private final List<File> files;

        public Directory(String name, Directory parent) {
            this.name = name;
            this.parent = parent;
            directories = new ArrayList<>();
            files = new ArrayList<>();
        }

        public Directory getParent() {
            return parent;
        }

        public void setParent(Directory parent) {
            this.parent = parent;
        }

        public Directory addDirectory(Directory directory) {
            directories.add(directory);
            directory.setParent(this);
            return directory;
        }

        public void addFile(long size, String name) {
            files.add(new File(name, size));
        }

        public long getSize() {
            long size = 0;
            for (Directory dir : directories) {
                size += dir.getSize();
            }

            for (File file : files) {
                size += file.getSize();
            }

            return size;
        }

        public List<Directory> flatten() {
            List<Directory> list = new ArrayList<>();
            for (Directory dir : directories) {
                list.addAll(dir.flatten());
            }

            list.add(this);

            return list;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class File {
        private final String name;
        private final long size;

        public File(String name, long size) {
            this.name = name;
            this.size = size;
        }

        public long getSize() {
            return size;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
