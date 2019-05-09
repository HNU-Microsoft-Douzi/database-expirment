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

    private client(String serverIp) {

        try {

            boolean flag = true;

            while (flag) {

                s = new Socket(serverIp, 8888);
                System.out.println("ftp.client connected success");
                System.out.println("please select which command you want to complete!");
                String[] comStr = {"get", "put", "cd", "dir", "quit"};
                for (String com : comStr) System.out.print(com + "  ");
                comStr = null;
                System.out.println();

                br = new BufferedReader(new InputStreamReader(System.in));
                // 去除字符串前后的空格
                String cmd = br.readLine().trim();

                DataOutputStream dos = new DataOutputStream(
                        new BufferedOutputStream(s.getOutputStream()));
                byte[] buf = cmd.getBytes();


                dos.writeUTF(cmd);
                dos.flush();

                switch (cmd) {
                    default:
                        break;
                    case "get":
                        get(serverIp);
                        break;
                    case "put":
                        put(serverIp);
                        break;
                    case "cd":
                        cd(serverIp);
                        break;
                    case "dir":
                        dir(serverIp);
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

    /**
     * 从服务端下载文件         【已经测试】
     * @param serverIp
     */
    private void get(String serverIp) {
        System.out.println("please input file path from ftp.server:");
        try {
            Socket s = new Socket(serverIp, 8888);
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
            String savePath = "src\\ftp.ftp_receive_dic";
            savePath = savePath + File.separator + dis.readUTF();
            DataOutputStream fileOut = new DataOutputStream(
                    new BufferedOutputStream(new FileOutputStream(savePath)));
            len = dis.readLong();

            System.out.println("file length:" + len + "  KB");
            System.out.println("start receive file!");

            while (true) {
                int read = 0;
                if (dis != null) {
                    read = dis.read(buf);
                }
                passedlen += read;
                if (read == -1) {
                    break;
                }
                System.out.println("file has received" + (passedlen * 100 / len) + "%");
                fileOut.write(buf, 0, read);
            }
            System.out.println("file received complete, file save path: " + savePath);
            fileOut.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dis.close();
            dos.close();
            s.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.println();
    }

    /**
     * 客户端向服务端提交文件  【已经测试】
     *
     * @param serverIp
     */
    private void put(String serverIp) {
        System.out.println("please input file path from ftp.client to ftp.server:");
        Socket s = null;
        try {
            s = new Socket(serverIp, 8888);

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
            e.printStackTrace();
        } finally {
            try {
                if (dis != null) dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        }
        System.out.println();
    }

    private void cd(String serverIp) {
        System.out.println("请输入要进入的目录：");
        try {
            Socket s = new Socket(serverIp, 8888);
            br = new BufferedReader(new InputStreamReader(System.in));
            String changedDir = br.readLine();

            DataOutputStream dos = new DataOutputStream(
                    new BufferedOutputStream(s.getOutputStream()));

            dos.writeUTF(changedDir);

            System.out.println("current dictionary:" + changedDir + "\n");
            dos.flush();
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
        }
    }

    private void dir(String serverIp) {
        System.out.println("folling is dictionary：");
        try {
            s = new Socket(serverIp, 8888);
            dis = new DataInputStream(new BufferedInputStream(s
                    .getInputStream()));

            int BUFSIZE = 8912;
            byte[] buf = new byte[BUFSIZE];

            while (true) {
                int data = 0;
                if (dis != null) {
                    data = dis.read(buf);
                    if (data == -1 || data == 0) {
                        System.out.println("jumped out");
                        break;
                    }
                    String str = new String(buf);
                    System.out.println(str);
                } else {
                    System.out.println("jumped out");
                    break;
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (dis != null) dis.close();
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

    public static void main(String[] args) {
        String ip = NetWorkUtils.getHostIp();
        new client(ip);
//		System.out.println(ip);
    }
}
