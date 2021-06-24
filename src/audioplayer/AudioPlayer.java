package audioplayer;

import jaco.mp3.player.MP3Player;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AudioPlayer {

    public static void main(String[] args) {
        MyFrame mf = new MyFrame();                              
        long stamp = System.currentTimeMillis();                 //stamp เวลาเอาไว้
        long timeNow = System.currentTimeMillis();             //ค่าเวลาปัจจุบัน
        long timeEnd = System.currentTimeMillis();
        try {
            while (true) {
                timeNow = System.currentTimeMillis();                                                                // นำ currentTime มาใช้ในการนับเวลา
                if (timeNow > stamp + 150) {                                                                        

                    String oldText = mf.lbl.getText();                                                                      
                    String newText = oldText.substring(1) + oldText.substring(0, 1);                            // คำแรกเริ่มจาก ตัวที่ 2 + ตัวแรก จะเหมือนกับว่ามันเดินอยู่
                    mf.lbl.setText(newText); 

                    stamp = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {

        }
    }

}

class MyFrame extends JFrame implements ActionListener {
    
    String audioType;                                                                    // เอาไว้เก็บชนิดของไฟล์เสียง ว่าเป็น MP3 File หรือ WAVE File
    MP3Player mp3 = new MP3Player();                                            //ต้อง add Library jaco-mp3 ต้องไปดาวน์โหลดมา

    AudioInputStream stream;                                                          //steam คือ ท่อสำหรับนำข้อมูลจากตำแหน่งของ File มายัง Buffer ของเรา
    Clip clip;                                                                                  // Buffer สำหรับเก็บไฟล์ที่ถูกส่งมาจากท่อ Stream
    JMenuBar mnB = new JMenuBar();
    JMenu mn = new JMenu("File");
    JMenuItem mnIOpen = new JMenuItem("Open");
    JLabel lbl = new JLabel(" ");
    JPanel pnl = new JPanel();
    JButton btnPlay = new JButton("Play");
    JButton btnStop = new JButton("Stop");
    JFileChooser fchOpen = new JFileChooser("C:/Sound");                  ////กำหนด Constructor เป็นตำแหน่งของ พาร์ธ เริ่มต้น
    File fileName;

    public MyFrame() {
        setTitle("MUSIC Player");
        setBounds(0, 0, 300, 180);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        mnB.add(mn);
        mn.add(mnIOpen);
        mnB.setBounds(0, 0, 300, 30);
        mnIOpen.addActionListener(this);                                                    //ต้องผูก Listener เป็น ActionListener เท่านั้น

        pnl.setBounds(0, 30, 300, 180);
        pnl.setLayout(null);

        lbl.setBounds(0, 0, 300, 60);
         lbl.setHorizontalAlignment((int) CENTER_ALIGNMENT);

        lbl.setBorder(BorderFactory.createLineBorder(Color.black));
        lbl.setFont(new Font("Kanit Thin", 1, 14));
        lbl.setForeground(Color.white);
        lbl.setOpaque(true);                            //ต้องกำหนดให้ Label ทึบแสงก่อนจึงจะกำหนดสี background ได้
        lbl.setBackground(Color.BLACK);
        btnPlay.setBounds(0, 60, 150, 60);
        btnPlay.addActionListener(this);
        btnStop.setBounds(150, 60, 150, 60);
        btnStop.addActionListener(this);
        pnl.add(lbl);
        pnl.add(btnPlay);
        pnl.add(btnStop);
        add(pnl);
        add(mnB);

        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnPlay) {                                             //เมื่อกดปุ่ม Play 
            if (audioType.equals("MP3 File")) {                                         //เช็ค ว่าชนิดของ File คือ Mp3 หรือไม่

                playMp3();                                                                      //เรียก Method       // playMp3
            } else if (audioType.equals("WAV File")) {                                //เช็ค ว่าชนิดของ File คือ Wav หรือไม่

                playWav();                                                                      //เรียก Method       // playWav
            } else {                                                                                //ไม่ได้เลือกไฟล์หรือ ไม่มีชนิดไฟล์ Wave หรือ Mp3
                System.out.println("ยังไม่ได้เลือกไฟล์");
            }
        } else if (e.getSource() == btnStop) {                                  //เมื่อกดปุ่น Stop
            if (audioType.equals("MP3 File")) {                                   //เช็ค ว่าชนิดของ File คือ Mp3 หรือไม่

                stopMp3();                                                              // หยุดการเล่นของ Mp3
            } else if (audioType.equals("WAV File")) {                          //เช็ค ว่าชนิดของ File คือ Wav หรือไม่
                stopWav();                                                               //หยุดการเล่นของ Mp3
            } else {
                System.out.println("ยังไม่ได้เลือกไฟล์");
            }
        }
        if (e.getSource() == mnIOpen) {

            FileNameExtensionFilter filter = new FileNameExtensionFilter("Audio", "mp3", "wav");              //ให้จำกัดว่าจะใช้ชนิดไฟล์ แบบไหนบ้าง ใส่ไว้ในตัวแปร filter
            fchOpen.setFileFilter(filter);                                                                                           //นำตัวแปร filter มาใช้                  

            fchOpen.setDialogTitle("Open");
            int retureVal = fchOpen.showOpenDialog(null);                                   // null หมายถึง แสดง Dailog show open ตรงกลาง Frame ถ้าจะให้แสดงกลาง Object ใดให้ใส่ Object นั้น แทน null
            if (retureVal == 0) {                                                           //ถ้าเลือกไฟล์สำเร็จให้คืนค่า เท่ากับ 0 ถ้าไม่สำเร็จให้คืนค่า เท่ากับ 1

                try {

                    fileName = (fchOpen.getSelectedFile());                             //นำ Path ของFile ที่เราเลือกมาเก็บไว้ในตัวแปร fileName
                    audioType = fchOpen.getTypeDescription(fileName);                   // นำชนิดของ File มาเก็บไว้ในตัวแปร audioType
                    System.out.println(audioType);
                    lbl.setText(fchOpen.getSelectedFile().getName());                       // set label เป็นชื่อไฟล์ที่เลือกมา
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        }

    }

    public void playMp3() {
        try {                                                               //ต้องใส่ Exception เพราะหากกำหนดให้หยุดเล่นแต่ไม่มีไฟล์เล่นอยู่จะ error
            if (clip.isRunning()) {                                                         //ถ้า จะเล่น Mp3 แล้ว wav เล่นอยู่ให้ปิดไฟล์ wav ก่อน 
                clip.stop();
            }}catch (Exception e) {
            } 
                mp3.addToPlayList(fileName);                                        //นำเพลงไปเก็บไว้ในรายการเพลง
                mp3.skipForward();                                                      //ไปยังเพลงถัดไป
                mp3.play();
            
        
        
    }

    public void stopMp3() {
        mp3.stop();
    }

    public void playWav() {
        try {                                                          //ต้องใส่ Exception เพราะหากกำหนดให้หยุดเล่นแต่ไม่มีไฟล์เล่นอยู่จะ error
            if (!mp3.isStopped())   {                                                                   //ถ้า จะเล่น wav แล้ว mp3 เล่นอยู่ให้ปิดไฟล์ wav ก่อน
                mp3.stop();
            }
        } catch (Exception e) {

        }
        try {
            // AudioInputStream stream;                                                          //steam คือ ท่อสำหรับนำข้อมูลจากตำแหน่งของ File มายัง Buffer ของเรา
            stream = AudioSystem.getAudioInputStream(fileName);
            clip = AudioSystem.getClip();                                                               //สร้าง Buffer สำหรับเล่น Audio 
            clip.open(stream);                                                                                       //นำไฟล์ที่ส่งมาจาก stream ส่งไปเก็บไว้ใน Buffer ที่ชื่อว่า clip
            clip.start();                                                                                               // เล่นเพลงจาก Buffer
        } catch (Exception ec) {
            System.out.println(ec);
        }
    }

    public void stopWav() {
        clip.stop();                                                                                                    //หยุดเล่นเพลง
        clip.close();                                                                                                   //คืนค่าหน่วยความจำของ Buffer ให้ระบบ
    }
}
