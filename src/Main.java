import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {
        File file = new File("C://savegames");
        if (file.mkdir()) System.out.print("Создана папка " + file.getName() + " по пути: " + file + "\n");
        List<String> list = Arrays.asList("C://savegames//save1.dat", "C://savegames//save2.dat", "C://savegames//save3.dat");
        saveGames(list.get(0), new GameProgress(500, 1, 10, 1000));
        saveGames(list.get(1), new GameProgress(700, 2, 25, 1500));
        saveGames(list.get(2), new GameProgress(900, 3, 40, 2000));
        zipFiles("C://savegames//zip.zip", list);
    }

    private static void saveGames(String str, GameProgress game) {
        // откроем выходной поток для записи в файл
        try (FileOutputStream fos = new FileOutputStream(str);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            // запишем экземпляр класса в файл
            oos.writeObject(game);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    private static void zipFiles(String zipWay, List<String> list) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipWay))) {
            for (String file : list) {
                //Считываем файл, который необходимо заархивировать
                FileInputStream fileFromZip = new FileInputStream(file);
                //Создаём в архиве файл с таким же названием
                File saveFile = new File(file);
                ZipEntry entry = new ZipEntry(saveFile.getName());
                //Добавляем его к архиву
                zout.putNextEntry(entry);
                //Считываем содержимое файла в массив
                byte[] buffer = new byte[fileFromZip.available()];
                fileFromZip.read(buffer);
                //Добавляем содержимое в файл архива
                zout.write(buffer);
                //Закрываем запись
                fileFromZip.close();
                zout.closeEntry();
                //Удаляем файл, который записался в архив
                if (saveFile.delete()) System.out.println("Удалён временный файл " + saveFile.getName());
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
