package FTP;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class client {
    private Socket s;
    private BufferedReader br;
    private DataOutputStream dos;
    private DataInputStream dis;

    private client(String serName) {

        try {

            boolean flag = true;

            while (flag) {

                s = new Socket(serName, 8888);
                System.out.println("client connected success");
                System.out.println("please select which command you want to complete!");
                String[] comStr = {"get", "put", "cd", "dir", "quit"};
                for (String com : comStr) System.out.print(com + "  ");
                comStr = null;
                br = new BufferedReader(new InputStreamReader(System.in));
                String cmd = br.readLine();

                DataOutputStream dos = new DataOutputStream(
                        new BufferedOutputStream(s.getOutputStream()));
                byte[] buf = cmd.getBytes();


                dos.writeUTF(cmd);
                dos.flush();

                switch (cmd) {
                    default:
                        break;
                    case "get":
                        get(serName);
                        break;
                    case "put":
                        put(serName);
                        break;
                    case "cd":
                        cd(serName);
                        break;
                    case "dir":
                        dir(serName);
                        break;
                    case "quit":
                        flag = false;
                        break;
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (dos != null) dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (s != null) s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void get(String serName) {
        System.out.println("please input get dir from server:");
        try {
            Socket s = new Socket(serName, 8888);
            br = new BufferedReader(new InputStreamReader(System.in));
            String downFile = br.readLine();
            dos = new DataOutputStream(
                    new BufferedOutputStream(s.getOutputStream()));
            dos.writeUTF(downFile);
            dos.flush();

            dis = new DataInputStream(new BufferedInputStream(s.getInputStream()));

            int bufferSize = 8192;
            byte[] buf = new byte[bufferSize];
            int passedlen = 0;

            long len = 0;
            String savePath = "D:\\clientTest";
            savePath = savePath + File.separator + dis.readUTF();
            DataOutputStream fileOut = new DataOutputStream(
                    new BufferedOutputStream(new FileOutputStream(savePath)));
            len = dis.readLong();

            System.out.println("文件的长度为:" + len + "  KB");
            System.out.println("开始接收文件!");

            while (true) {
                int read = 0;
                if (dis != null) {
                    read = dis.read(buf);
                }
                passedlen += read;
                if (read == -1) {
                    break;
                }
                System.out.println("文件接收了" + (passedlen * 100 / len) + "%");
                fileOut.write(buf, 0, read);
            }
            System.out.println("接收完成，文件存为" + savePath);
            fileOut.close();

        } catch (IOException e) {

        }
        try {
            dis.close();
            dos.close();
            s.close();
        } catch (IOException e1) {

        }

    }

    private void put(String serName) {
        System.out.println("put");
        Socket s = null;
        try {
            s = new Socket(serName, 8888);

            br = new BufferedReader(new InputStreamReader(System.in));
            String upFile = br.readLine();

            dos = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
            File file = new File(upFile);
            dos.writeUTF(file.getName());
            dos.flush();
            dos.writeLong(file.length());
            dos.flush();

            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(upFile)));

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

        } finally {

            try {
                dis.close();
                dos.close();
                s.close();
            } catch (IOException e) {

                e.printStackTrace();
            }

        }
    }

    private void cd(String serName) {
        System.out.println("请输入要进入的目录：");
        try {
            Socket s = new Socket(serName, 8888);
            br = new BufferedReader(new InputStreamReader(System.in));
            String changedDir = br.readLine();

            DataOutputStream dos = new DataOutputStream(
                    new BufferedOutputStream(s.getOutputStream()));

            dos.writeUTF(changedDir);
            dos.flush();
            dos.close();
            s.close();

        } catch (IOException e) {

        }
    }

    private void dir(String serName) {
        System.out.println("以下是目录：");
        try {
            s = new Socket(serName, 8888);
            dis = new DataInputStream(new BufferedInputStream(s
                    .getInputStream()));

            int BUFSIZE = 8912;
            byte[] buf = new byte[BUFSIZE];

            while (true) {
                int data = 0;
                if (dis != null) {
                    data = dis.read(buf);
                    if (data == -1 || data == 0) {
                        System.out.println("跳出");
                        break;
                    }
                    String str = new String(buf);
                    System.out.println(str);
                } else {
                    System.out.println("跳出");
                    break;
                }

            }

        } catch (IOException e) {
            System.out.println("错了");
        } finally {
            try {
                dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        String ip = NetWorkUtils.getHostIp();
        new client(ip);
//		System.out.println(ip);
    }
}
