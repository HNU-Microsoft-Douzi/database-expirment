import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.*;
import java.util.ArrayList;
import java.io.File;

/**
 * 主体逻辑：
 * 客户端会不断向服务端发送命令，服务端接收到并解析后，执行为相应的命令
 */

public class server {
    private ServerSocket ss = null;
    private DataOutputStream dos;
    private File rootDirectory;
    private DataInputStream dis;
    private ArrayList<File> fileArrayList = new ArrayList<>();
    private String sharedFileDirectory;
    private WeakReference<Configuration> weakCon;

    public static void main(String[] args) {
        System.out.println("start listening...");
        new server();
    }

    private server() {
        Class<Configuration> clz = Configuration.class;
        Configuration con = null;
        try {
            con = clz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if (con == null) throw new RuntimeException();
        weakCon = new WeakReference<>(con);
        this.sharedFileDirectory = weakCon.get().getSERVER_SHARED_DIR();
        con = null;
        try {
            ss = new ServerSocket(8888);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        if (ss == null) return;

        try {

            while (true) {

                boolean flag = true;

                while (flag) {
                    Socket s = ss.accept();
                    System.out.println("connect success!");
                    dis = new DataInputStream(new BufferedInputStream(s
                            .getInputStream()));
                    String cmd = dis.readUTF();

                    dis.close();
                    s.close();

                    System.out.println("cout：" + cmd);

                    switch (cmd) {
                        default:
                            break;
                        case "get":
                            get();
                            break;
                        case "put":
                            put();
                            break;
                        case "cd":
                            cd();
                            break;
                        case "dir":
                            dir();
                            break;
                        case "quit":
                            flag = false;
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 客户端传过来要读取的文件路径，服务端将对应路径下的文件数据转成流通过TCP信道传输给客户端
     */
    private void get() {
        System.out.println("client download file from server!");

        Socket s = null;
        try {
            s = ss.accept();
            dis = new DataInputStream(new BufferedInputStream(s
                    .getInputStream()));
            String filePath = dis.readUTF();
            System.out.println(filePath);

            dos = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));

            File file = new File(filePath);
            dos.writeUTF(file.getName());
            dos.flush();
            dos.writeLong(file.length());
            dos.flush();

            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)));

            int BUFSIZE = 8192;
            byte[] buf = new byte[BUFSIZE];

            while (true) {
                int read = 0;
                if (dis != null) {
                    read = dis.read(buf);
                } else {
                    System.out.println("no file founded!");
                    break;
                }
                if (read == -1) {
                    break;
                }
                dos.write(buf, 0, read);
            }
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (s != null) s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 客户端向服务端上传文件
     */
    private void put() {
        System.out.println("client uploading file to server!");
        DataOutputStream fileOut = null;
        try (Socket s = ss.accept()) {
            dis = new DataInputStream(new BufferedInputStream(s.getInputStream()));

            int bufferSize = 8192;
            byte[] buf = new byte[bufferSize];
            int passedlen = 0;

            long len = 0;
            String savePath = weakCon.get().getSERVER_RECEIVE_DIR();
            savePath = savePath + File.separator + dis.readUTF();
            fileOut = new DataOutputStream(
                    new BufferedOutputStream(new FileOutputStream(savePath)));
            len = dis.readLong();

            System.out.println("file size:" + len + " KB");
            System.out.println("start receive...");

            while (true) {
                int read = 0;
                if (dis != null) {
                    read = dis.read(buf);
                }
                passedlen += read;
                if (read == -1) {
                    break;
                }
                System.out.println("file has received:" + (passedlen * 100 / len) + "%");
                fileOut.write(buf, 0, read);
            }
            System.out.println("receive complete, file saved in:" + savePath);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOut != null) fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 查看服务端的文件目录
     */
    private void cd() {
        System.out.println("view server dictionary:");
        Socket s = null;
        try {
            s = ss.accept();
            dis = new DataInputStream(new BufferedInputStream(s
                    .getInputStream()));
            sharedFileDirectory = dis.readUTF();
            System.out.println("client request dictionary is:" + sharedFileDirectory);
            dir();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 显示服务端的所有目录
     * @throws IOException
     */
    private void dir() throws IOException {
        // 分享目录
        rootDirectory = new File(sharedFileDirectory);
        fileArrayList.clear();

        initFileArrayList();
//        StringBuilder builder = new StringBuilder();
        System.out.println("dictionary files are:");
        String direcFile = "";
        for (File aFileArrayList : fileArrayList) {
//            builder.append(aFileArrayList.getAbsolutePath()).append("\n");
            direcFile += aFileArrayList.getAbsolutePath() + "\n";
            System.out.println(aFileArrayList.getAbsolutePath());
        }
//        String direcFile = builder.toString();
        Socket s = null;
        try {
            s = ss.accept();
            dos = new DataOutputStream(
                    new BufferedOutputStream(s.getOutputStream()));
            byte[] buf = direcFile.getBytes();
            dos.write(buf);
            dos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            dos.close();

            if (s != null) s.close();
        }
    }

    /**
     * 初始化文件目录
     */
    private void initFileArrayList() {
        if (rootDirectory.isDirectory()) {
            File[] fileList = rootDirectory.listFiles();
            System.out.println("dictionary has " + fileList.length + " files");
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isFile()) {
                    fileArrayList.add(new File(fileList[i].getAbsolutePath()));
                } else if (fileList[i].isDirectory()) {
                    System.out.println("files:");
                    fileList[i].mkdir();//
                    rootDirectory = fileList[i];
                    initFileArrayList();
                }

            }
        }

    }
}
