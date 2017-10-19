/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 * ר��������Ч������
 *
 * @author Administrator
 */
public class UiUtil {

    private UiUtil() {
    }

    //�޸Ĵ����ͼ��
    public static void setFrameImage(JFrame jf) {
        //��ȡ���������
        //public static Toolkit getDefaultToolkit():��ȡĬ�Ϲ��߰��� 
        Toolkit tk = Toolkit.getDefaultToolkit();

        //����·����ȡͼƬ
        Image i = tk.getImage("src\\resource\\user.jpg");

        //����������ͼƬ
        jf.setIconImage(i);
    }
    
        public static void setFrameImage(JFrame jf,String imageName) {
        //��ȡ���������
        //public static Toolkit getDefaultToolkit():��ȡĬ�Ϲ��߰��� 
        Toolkit tk = Toolkit.getDefaultToolkit();

        //����·����ȡͼƬ
        Image i = tk.getImage("src\\resource\\"+imageName);

        //����������ͼƬ
        jf.setIconImage(i);
    }

    //���ô������
    public static void setFrameCenter(JFrame jf) {
        /*
         ˼·��
         A:��ȡ��Ļ�Ŀ�͸�
         B:��ȡ����Ŀ�͸�
         C:(����Ļ�Ŀ�-����Ŀ�)/2��(����Ļ�ĸ�-����ĸ�)/2��Ϊ����������ꡣ
         */
        //��ȡ���߶���
        Toolkit tk = Toolkit.getDefaultToolkit();

        //��ȡ��Ļ�Ŀ�͸�
        Dimension d = tk.getScreenSize();
        double srceenWidth = d.getWidth();
        double srceenHeigth = d.getHeight();

        //��ȡ����Ŀ�͸�
        int frameWidth = jf.getWidth();
        int frameHeight = jf.getHeight();

        //��ȡ�µĿ�͸�
        int width = (int) (srceenWidth - frameWidth) / 2;
        int height = (int) (srceenHeigth - frameHeight) / 2;

        //���ô�������
        jf.setLocation(width, height);
    }
}