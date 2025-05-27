package slotMachineGUI;

import javax.sound.sampled.*;
import java.io.File;

public class Sound {
    private static boolean enabled = true;
    private static Clip loopClip;
    private static Clip currentClip; // Quản lý âm thanh ngắn

    public static void setEnabled(boolean isEnabled) {
        enabled = isEnabled;
        if (!isEnabled) {
            stopLoop();
            stopCurrentSound();
        }
    }

    public static boolean isEnabled() {
        return enabled;
    }

    // Phát 1 lần, dừng âm cũ trước
    public static void playSound(String fileName) {
        if (!enabled) return;

        try {
            stopCurrentSound(); // Dừng nếu đang phát âm khác

            File file = new File("resources/SoundEffect/" + fileName);
            if (!file.exists()) {
                System.err.println("Không tìm thấy file: " + file.getAbsolutePath());
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            currentClip = AudioSystem.getClip();
            currentClip.open(audioStream);
            currentClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Phát loop (background, v.v.)
    public static void playLoop(String fileName) {
        if (!enabled) return;

        try {
            stopLoop();

            File file = new File("resources/SoundEffect/" + fileName);
            if (!file.exists()) {
                System.err.println("Không tìm thấy file: " + file.getAbsolutePath());
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            loopClip = AudioSystem.getClip();
            loopClip.open(audioStream);
            loopClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Dừng âm thanh hiện tại (chỉ playSound)
    public static void stopCurrentSound() {
        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
            currentClip.close();
        }
    }

    // Dừng loop (background music)
    public static void stopLoop() {
        if (loopClip != null && loopClip.isRunning()) {
            loopClip.stop();
            loopClip.close();
        }
    }
}
