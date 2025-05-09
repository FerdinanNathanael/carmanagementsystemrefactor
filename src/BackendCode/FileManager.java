
package BackendCode;

import java.io.*;
import java.util.ArrayList;
import java.util.function.Predicate;

public class FileManager<T> {
    private final String fileName;

    public FileManager(String fileName) {
        this.fileName = fileName;
    }

    public void saveAll(ArrayList<T> list) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            for (T obj : list) {
                out.writeObject(obj);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public ArrayList<T> loadAll() {
        ArrayList<T> list = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            while (true) {
                try {
                    T obj = (T) in.readObject();
                    list.add(obj);
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    System.out.println(e);
                }
            }
        } catch (IOException e) {
            // File not found is okay for first-time read
            if (!(e instanceof FileNotFoundException)) {
                System.out.println(e);
            }
        }
        return list;
    }

    public void saveOrUpdate(T obj, Predicate<T> match) {
        ArrayList<T> list = loadAll();
        boolean updated = false;
        for (int i = 0; i < list.size(); i++) {
            if (match.test(list.get(i))) {
                list.set(i, obj);
                updated = true;
                break;
            }
        }
        if (!updated) list.add(obj);
        saveAll(list);
    }

    public void remove(Predicate<T> match) {
        ArrayList<T> list = loadAll();
        list.removeIf(match);
        saveAll(list);
    }

    public ArrayList<T> search(Predicate<T> match) {
        ArrayList<T> list = loadAll();
        ArrayList<T> result = new ArrayList<>();
        for (T obj : list) {
            if (match.test(obj)) {
                result.add(obj);
            }
        }
        return result;
    }
}
