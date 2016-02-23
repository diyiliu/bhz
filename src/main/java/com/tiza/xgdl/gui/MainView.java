package com.tiza.xgdl.gui;

import com.tiza.xgdl.Main;
import com.tiza.xgdl.util.Common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Description: MainView
 * Author: DIYILIU
 * Update: 2016-01-11 10:33
 */
public class MainView {

    private JPanel rootPanel;
    private JButton runButton;
    private JTextField uartTextField;
    private JTextField u_driverTextField;
    private JTextField u_urlTextField;
    private JTextField u_usernameTextField;
    private JTextField u_passwordTextField;
    private JTextField r_driverTextField;
    private JTextField r_urlTextField;
    private JTextField r_usernameTextField;
    private JTextField r_passwordTextField;
    private JPanel uartPanel;
    private JLabel uartLabel;
    private JLabel u_driverLabel;
    private JLabel u_urlLabel;
    private JLabel u_usernameLabel;
    private JLabel u_passwordLabel;
    private JLabel r_driverLabel;
    private JLabel r_urlLabel;
    private JLabel r_usernameLabel;
    private JLabel r_passwordLabel;


    public static void show() {

        try {
            // 设置样式
            String ui = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(ui);

        } catch (Exception e) {
            e.printStackTrace();
        }

        final MainView mainView = new MainView();
        final JFrame frame = new JFrame("配置面板");

        frame.setContentPane(mainView.rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


        // 设置窗口居中
        int WIDTH = frame.getWidth();
        int HEIGHT = frame.getHeight();
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screamSize = kit.getScreenSize();
        frame.setBounds((screamSize.width - WIDTH) / 2, (screamSize.height - HEIGHT) / 2, WIDTH, HEIGHT);

        mainView.initConfig();

        mainView.runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                mainView.setConfig();
                frame.dispose();
                Main.run();
            }
        });
    }


    private void initConfig() {

        Map<String, String> config = getProperties("config.properties");

        this.uartTextField.setText(config.get("com"));

        config = getProperties("user.properties");
        this.u_driverTextField.setText(config.get("driverClassName"));
        this.u_urlTextField.setText(config.get("url"));
        this.u_usernameTextField.setText(config.get("username"));
        this.u_passwordTextField.setText(config.get("password"));

        config = getProperties("report.properties");
        this.r_driverTextField.setText(config.get("driverClassName"));
        this.r_urlTextField.setText(config.get("url"));
        this.r_usernameTextField.setText(config.get("username"));
        this.r_passwordTextField.setText(config.get("password"));

    }


    private void setConfig() {

        final String uart = this.uartTextField.getText().trim();

        setProperties("config.properties", new HashMap<String, String>() {
            {
                put("com", uart);
            }
        });

        final String uDriver = this.u_driverTextField.getText().trim();
        final String uUrl = this.u_urlTextField.getText().trim();
        final String uUsername = this.u_usernameTextField.getText().trim();
        final String uPassword = this.u_passwordTextField.getText().trim();

        setProperties("user.properties", new HashMap<String, String>() {
            {
                put("driverClassName", uDriver);
                put("url", uUrl);
                put("username", uUsername);
                put("password", uPassword);
            }
        });

        final String rDriver = this.r_driverTextField.getText().trim();
        final String rUrl = this.r_urlTextField.getText().trim();
        final String rUsername = this.r_usernameTextField.getText().trim();
        final String rPassword = this.r_passwordTextField.getText().trim();

        setProperties("report.properties", new HashMap<String, String>() {
            {
                put("driverClassName", rDriver);
                put("url", rUrl);
                put("username", rUsername);
                put("password", rPassword);
            }
        });
    }

    public  Map<String, String> getProperties(String file) {

        Map map = new HashMap();
        try {
            InputStream inStream = new FileInputStream(new File(Common.getFilePath(file)));

            Properties properties = new Properties();
            properties.load(inStream);
            inStream.close();

            Set<String> names = properties.stringPropertyNames();

            for (String name : names) {
                map.put(name, properties.getProperty(name));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }


    public  void setProperties(String file, Map<String, String> map) {

        try {
            OutputStream outStream = new FileOutputStream(new File(Common.getFilePath(file)));

            Properties properties = new Properties();

            Set<String> keySet = map.keySet();

            for (String key : keySet) {
                properties.setProperty(key, map.get(key));
            }

            properties.store(outStream, "reset config!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InputStream getFileInputStream(String file) {

        return this.getClass().getClassLoader().getResourceAsStream(file);
    }


    public static void test(){


        /**
        String path = MainView.class.getClassLoader().getResource("config.properties").getPath();

        System.out.println(path);

        System.out.println(new File(path).exists());

        path = MainView.class.getClassLoader().getResource("").getPath();

        System.out.println(path);*/

        Class clazz = MainView.class;

        System.out.println(clazz.getName());
        System.out.println(clazz.getResource("MainView.class"));
        System.out.println(System.getProperty("user.dir"));
        File file = new File(System.getProperty("user.dir") + "/config.properties" );
        System.out.println(file.exists());
        System.out.println(MainView.class.getClassLoader().getResourceAsStream("config.properties"));
        System.out.println(MainView.class.getClassLoader().getResource("config.properties"));
    }



    public static void main(String[] args) {
        show();
    }
}
