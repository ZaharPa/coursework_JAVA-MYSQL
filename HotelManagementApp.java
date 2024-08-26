import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Map;

public class HotelManagementApp {

    private static final String URL = "jdbc:mysql://localhost:3306/course";
    private static final String USER = "root";
    private static final String PASSWORD = "123";
    
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }


    private User currentUser;

    private Map<String, String> adminCredentials = new HashMap<>();
    private Map<String, String> userCredentials = new HashMap<>();

    public HotelManagementApp() {
        // Add some mock credentials
        adminCredentials.put("admin", "adminpass");
        userCredentials.put("user", "userpass");

        JFrame loginFrame = new JFrame("Login");
        loginFrame.setSize(300, 200);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new GridLayout(3, 2));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        loginFrame.add(userLabel);
        loginFrame.add(userField);
        loginFrame.add(passLabel);
        loginFrame.add(passField);
        loginFrame.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());

                if (adminCredentials.containsKey(username) && adminCredentials.get(username).equals(password)) {
                    currentUser = new User(username, UserRole.ADMIN);
                    loginFrame.dispose();
                    showMainFrame();
                } else if (userCredentials.containsKey(username) && userCredentials.get(username).equals(password)) {
                    currentUser = new User(username, UserRole.USER);
                    loginFrame.dispose();
                    showMainFrame();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid credentials!");
                }
            }
        });

        loginFrame.setVisible(true);
    }

    private void showMainFrame() {
        JFrame frame = new JFrame("Hotel Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 900);
        frame.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Display Panel
        JPanel displayPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        displayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        displayPanel.setBackground(new Color(240, 248, 255));

        JButton displayHotelDataButton = createStyledButton("Вивести дані готелю");
        JButton displayClientDataButton = createStyledButton("Вивести дані про клієнтів");
        JButton displayLetterClientDataButton = createStyledButton("Вивести дані про клієнтів, чиї прізвище починаються з певної літери");
        JButton displayStaffDataButton = createStyledButton("Вивести дані про працівників");
        JButton displayAddServDataButton = createStyledButton("Вивести дані про додаткові послуги");
        JButton displayRoomWithCommentButton = createStyledButton("Вивести дані про кімнати та коментарі про статус");
        JButton displayPaymentNotFinnishButton = createStyledButton("Вивести дані про незавершені платежі");
        JButton displayBookingActiveButton = createStyledButton("Вивести дані про активні бронювання");
        JButton displayBookingPerDataButton = createStyledButton("Вивести дані про бронювання за певний період");
        JButton displayPaymentPerMonthButton = createStyledButton("Вивести дані про кількість платежів клієнтів за місяць");
        JButton displayHotelsWithMinRoomPriceButton = createStyledButton("Показати готелі з найнижчими цінами кімнат");

        displayPanel.add(displayHotelDataButton);
        displayPanel.add(displayClientDataButton);
        displayPanel.add(displayLetterClientDataButton);
        displayPanel.add(displayStaffDataButton);
        displayPanel.add(displayAddServDataButton);
        displayPanel.add(displayRoomWithCommentButton);
        displayPanel.add(displayPaymentNotFinnishButton);
        displayPanel.add(displayBookingActiveButton);
        displayPanel.add(displayBookingPerDataButton);
        displayPanel.add(displayPaymentPerMonthButton);
        displayPanel.add(displayHotelsWithMinRoomPriceButton);

        tabbedPane.addTab("Відображення даних", displayPanel);

        if (currentUser.getRole() == UserRole.ADMIN) {
            // Add Panel
            JPanel addPanel = new JPanel(new GridLayout(0, 1, 10, 10));
            addPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            addPanel.setBackground(new Color(240, 255, 240));

            JButton addClientDataButton = createStyledButton("Додати дані про клієнта");
            JButton addStaffDataButton = createStyledButton("Додати дані про працівника");
            JButton addDateBookingButton = createStyledButton("Додати дані про дату бронювання");
            JButton addBookingButton = createStyledButton("Додати дані про бронювання");
            JButton addPaymentButton = createStyledButton("Додати дані про платіж");

            addPanel.add(addClientDataButton);
            addPanel.add(addStaffDataButton);
            addPanel.add(addDateBookingButton);
            addPanel.add(addBookingButton);
            addPanel.add(addPaymentButton);

            tabbedPane.addTab("Додавання даних", addPanel);

            // Edit Panel
            JPanel editPanel = new JPanel(new GridLayout(0, 1, 10, 10));
            editPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            editPanel.setBackground(new Color(255, 248, 220));

            JButton editClientDataButton = createStyledButton("Редагувати дані про клієнта");
            JButton editBookingStatusButton = createStyledButton("Змінити статус бронювання");
            JButton editPaymentStatusButton = createStyledButton("Змінити статус платежу");
            JButton editRoomStatusButton = createStyledButton("Змінити статус кімнати");

            editPanel.add(editClientDataButton);
            editPanel.add(editBookingStatusButton);
            editPanel.add(editPaymentStatusButton);
            editPanel.add(editRoomStatusButton);

            tabbedPane.addTab("Редагування даних", editPanel);

            // Delete Panel
            JPanel deletePanel = new JPanel(new GridLayout(0, 1, 10, 10));
            deletePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            deletePanel.setBackground(new Color(255, 240, 245));

            JButton deleteStaffDataButton = createStyledButton("Видалити працівника");
            JButton deleteBookingButton = createStyledButton("Видалити бронювання");

            deletePanel.add(deleteStaffDataButton);
            deletePanel.add(deleteBookingButton);

            tabbedPane.addTab("Видалення даних", deletePanel);
            addClientDataButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String name = JOptionPane.showInputDialog(frame, "Введіть ПІБ:");
                    String phoneNumber = JOptionPane.showInputDialog(frame, "Введіть номер телефону:");
                    String dateBirth = JOptionPane.showInputDialog(frame, "Введіть дату народження(рік-місяць-день):");
                    String email = JOptionPane.showInputDialog(frame, "Введіть електрону пошту:");

                    if (name != null && phoneNumber != null && dateBirth != null && email != null) {
                        int rowsAffected = addClientData(name, phoneNumber, dateBirth, email);
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(frame, "Дані клієнта успішно додано.", "Успішно", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Не вдалося додати дані клієнта.", "Помилка", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
            
            addStaffDataButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	String idStr = JOptionPane.showInputDialog(frame, "Введіть id працівника:");
                    String name = JOptionPane.showInputDialog(frame, "Введіть ПІБ:");
                    String phoneNumber = JOptionPane.showInputDialog(frame, "Введіть номер телефону:");
                    String dateBirth = JOptionPane.showInputDialog(frame, "Введіть дату народження(рік-місяць-день):");
                    String dateStart = JOptionPane.showInputDialog(frame, "Введіть дату початку роботи(рік-місяць-день):");
                    String position = JOptionPane.showInputDialog(frame, "Введіть посаду:");
                    String hotelIdStr = JOptionPane.showInputDialog(frame, "Введіть id готелю:");

                    if (idStr != null && name != null && phoneNumber != null && dateBirth != null && dateStart != null && position != null && hotelIdStr != null) {
                    	int id = Integer.parseInt(idStr);
                    	int hotelId = Integer.parseInt(hotelIdStr);
                        int rowsAffected = addStaffData(id, name, phoneNumber, dateBirth, dateStart, position, hotelId);
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(frame, "Дані працівника успішно додано.", "Успішно", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Не вдалося додати дані працівника.", "Помилка", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
            
            addDateBookingButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	String startDate = JOptionPane.showInputDialog(frame, "Введіть дату початку бронювання (рік-місяць-день):");
                    String endDate = JOptionPane.showInputDialog(frame, "Введіть дату кінця бронювання (рік-місяць-день):");

                    if (startDate != null && endDate != null) {
                    	int rowsAffected = addDateBookingData(startDate, endDate);
                        
                    	if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(frame, "Бронювання успішно додано.", "Успішно", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Не вдалося додати бронювання.", "Помилка", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
            
            addBookingButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	String status = JOptionPane.showInputDialog(frame, "Введіть статус бронювання:");
                	String roomIdStr = JOptionPane.showInputDialog(frame, "Введіть номер кімнати:");
                    String durationStayIdStr = JOptionPane.showInputDialog(frame, "Введіть ID терміну проживання:");
                    String addSerIdStr = JOptionPane.showInputDialog(frame, "Введіть ID додаткових послуг:");
                    String clientIdStr = JOptionPane.showInputDialog(frame, "Введіть ID клієнта:");
                    String responsibleIdStr = JOptionPane.showInputDialog(frame, "Введіть ID відповідального працівника:");

                    if (status != null && roomIdStr != null && durationStayIdStr != null && addSerIdStr != null && clientIdStr != null && responsibleIdStr != null) {
                    	int roomId = Integer.parseInt(roomIdStr);
                    	int durationStayId = Integer.parseInt(durationStayIdStr);
                    	int addSerId = Integer.parseInt(addSerIdStr);
                    	int clientId = Integer.parseInt(clientIdStr);
                    	int responsibleId = Integer.parseInt(responsibleIdStr);
                    	
                    	int rowsAffected = addBookingData(status, roomId, durationStayId, addSerId, clientId, responsibleId);
                        
                    	if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(frame, "Бронювання успішно додано.", "Успішно", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Не вдалося додати бронювання.", "Помилка", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

            addPaymentButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String clientId = JOptionPane.showInputDialog(frame, "Введіть ID клієнта:");
                    String bookingId = JOptionPane.showInputDialog(frame, "Введіть ID бронювання:");
                    String amountStr  = JOptionPane.showInputDialog(frame, "Введіть суму платежу:");
                    String datePayment = JOptionPane.showInputDialog(frame, "Введіть дату платежу (рік-місяць-день):");
                    String status = JOptionPane.showInputDialog(frame, "Введіть статус платежу:");

                    if (clientId != null && bookingId != null && amountStr != null && datePayment != null && status != null) {
                    	int clientIdInt = Integer.parseInt(clientId);
                    	int bookingIdInt = Integer.parseInt(bookingId);
                    	float amount = Float.parseFloat(amountStr);
                    	
                        int rowsAffected = addPaymentData(clientIdInt, bookingIdInt, amount, datePayment, status);
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(frame, "Платіж успішно додано.", "Успішно", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Не вдалося додати платіж.", "Помилка", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
            
            editClientDataButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	String clientIdStr = JOptionPane.showInputDialog(frame, "Введіть id клієнта:");
                    String name = JOptionPane.showInputDialog(frame, "Введіть ПІБ:");
                    String phoneNumber = JOptionPane.showInputDialog(frame, "Введіть номер телефону:");
                    String dateBirth = JOptionPane.showInputDialog(frame, "Введіть дату народження(рік-місяць-день):");
                    String email = JOptionPane.showInputDialog(frame, "Введіть електрону пошту:");

                    if (clientIdStr!= null && name != null && phoneNumber != null && dateBirth != null && email != null) {
                    	int clientId = Integer.parseInt(clientIdStr);
                        int rowsAffected = editClientData(clientId, name, phoneNumber, dateBirth, email);
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(frame, "Дані клієнта успішно оновлено.", "Успішно", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Не вдалося оновити дані клієнта.", "Помилка", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
            
            editBookingStatusButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	String bookingIdStr  = JOptionPane.showInputDialog(frame, "Введіть id бронювання:");

                    if (bookingIdStr!= null) {
                    	int bookingId = Integer.parseInt(bookingIdStr);
                    	 String[] statuses = { "Активне",  "Скасоване", "Завершене" };
                    	 String newStatus = (String) JOptionPane.showInputDialog(frame, "Виберіть новий статус бронювання:",
                                 "Редагування статусу бронювання", JOptionPane.QUESTION_MESSAGE, null, statuses, statuses[0]);

                         if (newStatus != null) {
                             editBookingStatus(bookingId, newStatus);
                             JOptionPane.showMessageDialog(frame, "Статус бронювання успішно оновлено.", "Успішно", JOptionPane.INFORMATION_MESSAGE);
                         } else {
                             JOptionPane.showMessageDialog(frame, "Не вдалося оновити статус бронювання.", "Помилка", JOptionPane.ERROR_MESSAGE);
                         }
                    }
                }
            });
            
            editPaymentStatusButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	String paymentIdStr  = JOptionPane.showInputDialog(frame, "Введіть id статусу:");

                    if (paymentIdStr != null) {
                    	int paymentId = Integer.parseInt(paymentIdStr);
                    	 String[] statuses = { "Активний",  "Скасовано", "Завершено" };
                    	 String newStatus = (String) JOptionPane.showInputDialog(frame, "Виберіть новий статус платежу:",
                                 "Редагування статусу платежу", JOptionPane.QUESTION_MESSAGE, null, statuses, statuses[0]);

                         if (newStatus != null) {
                             editPaymentStatus(paymentId, newStatus);
                             JOptionPane.showMessageDialog(frame, "Статус платежу успішно оновлено.", "Успішно", JOptionPane.INFORMATION_MESSAGE);
                         } else {
                             JOptionPane.showMessageDialog(frame, "Не вдалося оновити статус платежу.", "Помилка", JOptionPane.ERROR_MESSAGE);
                         }
                    }
                }
            });
            
            editRoomStatusButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	String roomIdStr  = JOptionPane.showInputDialog(frame, "Введіть номер кімнати:");

                    if (roomIdStr != null) {
                    	int roomId = Integer.parseInt(roomIdStr);
                    	 String[] statuses = { "Вільна",  "Зайнята", "Тимчасово недоступна" };
                    	 String newStatus = (String) JOptionPane.showInputDialog(frame, "Виберіть новий статус кімнати:",
                                 "Редагування статусу кімнати", JOptionPane.QUESTION_MESSAGE, null, statuses, statuses[0]);

                         if (newStatus != null) {
                             editRoomStatus(roomId, newStatus);
                             JOptionPane.showMessageDialog(frame, "Статус кімнати успішно оновлено.", "Успішно", JOptionPane.INFORMATION_MESSAGE);
                         } else {
                             JOptionPane.showMessageDialog(frame, "Не вдалося оновити статус кімнати.", "Помилка", JOptionPane.ERROR_MESSAGE);
                         }
                    }
                }
            });

            deleteStaffDataButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String staffIdStr = JOptionPane.showInputDialog(frame, "Введіть ID працівника для видалення:");
                    if (staffIdStr != null) {
                        int staffId = Integer.parseInt(staffIdStr);
                        int rowsAffected = deleteStaffData(staffId);
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(frame, "Працівника успішно видалено.", "Успішно", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Не вдалося видалити працівника.", "Помилка", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
            

            deleteBookingButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String bookingIdStr = JOptionPane.showInputDialog(frame, "Введіть ID бронювання для видалення:");
                    if (bookingIdStr != null) {
                        int bookingId = Integer.parseInt(bookingIdStr);
                        int rowsAffected = deleteBooking(bookingId);
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(frame, "Бронювання успішно видалено.", "Успішно", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Не вдалося видалити бронювання.", "Помилка", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
        }

        frame.add(tabbedPane, BorderLayout.CENTER);

        displayHotelDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> hotelData = getHotelData();
                String result = String.join("\n", hotelData);
                JOptionPane.showMessageDialog(frame, result);
            }
        });

        displayClientDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> clientData = getClientData();
                StringBuilder result = new StringBuilder();
                for (String data : clientData) {
                    result.append(data).append("\n");
                }
                displayClientData(result.toString());
            }
        });
        
        displayLetterClientDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstLetter = JOptionPane.showInputDialog(frame, "Введіть літеру з якої починається прізвище клієнтів:");
                if (firstLetter != null) {
                    List<String> letterClientData = getLetterClientData(firstLetter);
                    StringBuilder result = new StringBuilder();
                    for (String data : letterClientData) {
                        result.append(data).append("\n");
                    }
                    displayLetterClientData(result.toString());
                }
            }
        });
        
        displayStaffDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> staffData = getStaffData();
                StringBuilder result = new StringBuilder();
                for (String data : staffData) {
                    result.append(data).append("\n");
                }
                displayStaffData(result.toString());
            }
        });
        
        displayAddServDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> addServData = getAddServData();
                StringBuilder result = new StringBuilder();
                for (String data : addServData) {
                    result.append(data).append("\n");
                }
                displayAddServData(result.toString());
            }
        });
        
        displayRoomWithCommentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> freeRoomData = getRoomWithCommentData();
                StringBuilder result = new StringBuilder();
                for (String data : freeRoomData) {
                    result.append(data).append("\n");
                }
                displayRoomWithCommentData(result.toString());
            }
        });
        
        displayPaymentNotFinnishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> paymentNotFinnishData = getPaymentNotFinnishData();
                StringBuilder result = new StringBuilder();
                for (String data : paymentNotFinnishData) {
                    result.append(data).append("\n");
                }
                displayPaymentNotFinnishData(result.toString());
            }
        });
        
        displayBookingActiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> BookingActiveData = getBookingActiveData();
                StringBuilder result = new StringBuilder();
                for (String data : BookingActiveData) {
                    result.append(data).append("\n");
                }
                displayBookingActiveData(result.toString());
            }
        });

        displayBookingPerDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String startDate = JOptionPane.showInputDialog(frame, "Введіть дату початку (YYYY-MM-DD):");
                String endDate = JOptionPane.showInputDialog(frame, "Введіть дату кінця (YYYY-MM-DD):");
                if (startDate != null && endDate != null) {
                    List<String> bookingPerData = getBookingPerData(startDate, endDate);
                    StringBuilder result = new StringBuilder();
                    for (String data : bookingPerData) {
                        result.append(data).append("\n");
                    }
                    displayBookingPerData(result.toString());
                }
            }
        });
        
        displayPaymentPerMonthButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String month = JOptionPane.showInputDialog(frame, "Введіть номер місяцю:");
                String year = JOptionPane.showInputDialog(frame, "Введіть рік:");
                if (month != null) {
                    List<String> paymentPerMonth = getPaymentPerMonth(month, year);
                    StringBuilder result = new StringBuilder();
                    for (String data : paymentPerMonth) {
                        result.append(data).append("\n");
                    }
                    displayPaymentPerMonth(result.toString());
                }
            }
        });
        
        displayHotelsWithMinRoomPriceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> hotelsWithMinPrice = getHotelsWithMinRoomPrice();
                String result = String.join("\n", hotelsWithMinPrice);
                JOptionPane.showMessageDialog(frame, result);
            }
        });


        
       
        

        frame.setVisible(true);
        
    }
    
    

    public static void createConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection to database established successfully.");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public List<String> getHotelData() {
        List<String> hotelData = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            String query = "SELECT * FROM готель";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id_готелю");
                String name = resultSet.getString("Назва");
                String address = resultSet.getString("Адреса");
                String phoneNumber = resultSet.getString("Номер_телефону");
                String foundationDate = resultSet.getString("Дата_заснування");
                String owner = resultSet.getString("Власник");

                String hotelInfo = String.format(
                    "ID: %d\nНазва: %s\nАдреса: %s\nНомер телефону: %s\nДата заснування: %s\nВласник: %s\n---------------------------",
                    id, name, address, phoneNumber, foundationDate, owner
                );

                hotelData.add(hotelInfo);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return hotelData;
    }

    public List<String> getClientData() {
        List<String> clientData = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            String query = "SELECT * FROM клієнт ";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id_клієнта");
                String fullName = resultSet.getString("ПІБ");
                String phoneNumber = resultSet.getString("Номер_телефону");
                String birthDate = resultSet.getString("Дата_народження");
                String email = resultSet.getString("Електрона_пошта");

                String clientInfo = String.format(
                    "ID: %d\nПІБ: %s\nНомер телефону: %s\nДата народження: %s\nЕлекторна пошта: %s\n---------------------------",
                    id, fullName, phoneNumber, birthDate, email
                );

                clientData.add(clientInfo);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return clientData;
    }

    private void displayClientData(String data) {
        JFrame frame = new JFrame("Інформація про клієнтів");
        JTextArea textArea = new JTextArea(data);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14)); 
        textArea.setBackground(new Color(240, 240, 240));
        textArea.setForeground(Color.BLACK); 
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);
        frame.setSize(400, 600);
        frame.setVisible(true);
    }

    
    public List<String> getLetterClientData(String firstLetter) {
        List<String> clientData = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            String query = String.format( "SELECT * FROM клієнт WHERE ПІБ LIKE '%s%%'", firstLetter);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id_клієнта");
                String fullName = resultSet.getString("ПІБ");
                String phoneNumber = resultSet.getString("Номер_телефону");
                String birthDate = resultSet.getString("Дата_народження");
                String email = resultSet.getString("Електрона_пошта");

                String clientInfo = String.format(
                    "ID: %d\nПІБ: %s\nНомер телефону: %s\nДата народження: %s\nЕлекторна пошта: %s\n---------------------------",
                    id, fullName, phoneNumber, birthDate, email
                );

                clientData.add(clientInfo);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return clientData;
    }

    private void displayLetterClientData(String data) {
        JFrame frame = new JFrame("Інформація про клієнтів");
        JTextArea textArea = new JTextArea(data);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14)); 
        textArea.setBackground(new Color(240, 240, 240));
        textArea.setForeground(Color.BLACK); 
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);
        frame.setSize(400, 600);
        frame.setVisible(true);
        }
    
    public List<String> getStaffData() {
        List<String> staffData = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            String query = "SELECT * FROM працівник JOIN готель ON працівник.готель_id = готель.id_готелю ORDER BY готель.id_готелю";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id_працівника");
                String fullName = resultSet.getString("ПІБ");
                String phoneNumber = resultSet.getString("Номер_телефону");
                String birthDate = resultSet.getString("Дата_народження");
                String startDate = resultSet.getString("Дата_початку_роботи");
                String position = resultSet.getString("Посада");
                String hotel = resultSet.getString("Назва");

                String staffInfo = String.format(
                    "ID: %d\nПІБ: %s\nНомер телефону: %s\nДата народження: %s\nДата початку роботи: %s\nПосада: %s\nГотель: %s\n---------------------------",
                    id, fullName, phoneNumber, birthDate, startDate, position, hotel
                );

                staffData.add(staffInfo);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return staffData;
    }
    
    private void displayStaffData(String data) {
        JFrame frame = new JFrame("Інформація про працівників");
        JTextArea textArea = new JTextArea(data);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14)); 
        textArea.setBackground(new Color(240, 240, 240));
        textArea.setForeground(Color.BLACK); 
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);
        frame.setSize(400, 600);
        frame.setVisible(true);
    }

    public List<String> getAddServData() {
        List<String> addServData = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            String query = "SELECT * FROM додаткові_послуги";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id_послуги");
                String name = resultSet.getString("Назва");
                float price = resultSet.getFloat("Ціна");
                String describe = resultSet.getString("Опис");

                String clientInfo = String.format(
                    "ID: %d\nНазва: %s\nЦіна: %f\nОпис: %s\n---------------------------",
                    id, name, price, describe
                );

                addServData.add(clientInfo);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return addServData;
    }

    private void displayAddServData(String data) {
        JFrame frame = new JFrame("Інформація про додаткові послуги");
        JTextArea textArea = new JTextArea(data);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14)); 
        textArea.setBackground(new Color(240, 240, 240));
        textArea.setForeground(Color.BLACK); 
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);
        frame.setSize(400, 600);
        frame.setVisible(true);
    }
    
    public List<String> getRoomWithCommentData() {
    	List<String> roomData = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            String query = "SELECT id_кімнати, Тип, Розмір, Ціна, Опис_зручностей, 'Вільна кімната' AS Коментар " +
                           "FROM кімната " +
                           "WHERE Статус = 'Вільна' " +
                           "UNION " +
                           "SELECT id_кімнати, Тип, Розмір, Ціна, Опис_зручностей, 'Зайнята кімната' AS Коментар " +
                           "FROM кімната " +
                           "WHERE Статус = 'Зайнята'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id_кімнати");
                String type = resultSet.getString("Тип");
                String size = resultSet.getString("Розмір");
                float price = resultSet.getFloat("Ціна");
                String describe = resultSet.getString("Опис_зручностей");
                String comment = resultSet.getString("Коментар");

                String roomInfo = String.format(
                    "ID: %d\nТип: %s\nРозмір: %s\nЦіна: %f\nОпис зручностей: %s\nКоментар: %s\n---------------------------",
                    id, type, size, price, describe, comment
                );

                roomData.add(roomInfo);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return roomData;
    }

    private void displayRoomWithCommentData(String data) {
        JFrame frame = new JFrame("Інформація про кімнати");
        JTextArea textArea = new JTextArea(data);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14)); 
        textArea.setBackground(new Color(240, 240, 240));
        textArea.setForeground(Color.BLACK); 
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);
        frame.setSize(400, 600);
        frame.setVisible(true);
    }
    
    public List<String> getPaymentNotFinnishData() {
        List<String> paymentNotFinnishData = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            String query = "SELECT платіж.*, клієнт.ПІБ " +
                    "FROM платіж " +
                    "LEFT JOIN клієнт ON платіж.Відправник_id = клієнт.id_клієнта " +
                    "WHERE платіж.Статус != 'Завершено'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
            	int paymentId = resultSet.getInt("id_платежу");
                int senderId = resultSet.getInt("Відправник_id");
                int bookingId = resultSet.getInt("Бронювання_id");
                float amount = resultSet.getFloat("Сума");
                Date date = resultSet.getDate("Дата");
                String status = resultSet.getString("Статус");
                String senderName = resultSet.getString("ПІБ");

                String paymentNotFinnishInfo = String.format(
                    "ID платежу: %d\nВідправник ID: %d\nБронювання ID: %d\nСума: %f\nДата: %s\nСтатус: %s\nПІБ відправника: %s\n---------------------------",
                    paymentId, senderId, bookingId, amount, date, status, senderName
                );

                paymentNotFinnishData.add(paymentNotFinnishInfo);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return paymentNotFinnishData;
    }

    private void displayPaymentNotFinnishData(String data) {
        JFrame frame = new JFrame("Інформація про незавершенні платежі");
        JTextArea textArea = new JTextArea(data);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14)); 
        textArea.setBackground(new Color(240, 240, 240));
        textArea.setForeground(Color.BLACK); 
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);
        frame.setSize(400, 600);
        frame.setVisible(true);
    }
    
    public List<String> getBookingActiveData() {
        List<String> bookingActive = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            String query = "SELECT * FROM бронювання JOIN працівник ON бронювання.Відповідальний_id = працівник.id_працівника " +
                "JOIN клієнт ON бронювання.Мешканець_id = клієнт.id_клієнта "+
                "JOIN дата_бронювання ON бронювання.Термін_проживання_id = дата_бронювання.id_дати_бронювання "+
                "WHERE Статус = 'Активне'";
                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
                
      
        while (resultSet.next()) {
            int id = resultSet.getInt("id_бронювання");
            String status = resultSet.getString("Статус");
            String client = resultSet.getString("клієнт.ПІБ");
            int roomNumber = resultSet.getInt("Кімната_id");
            String startDateResult = resultSet.getString("Дата_початку");
            String endDateResult = resultSet.getString("Дата_кінця");
            String bookingPeriod = resultSet.getString("Термін_проживання");
            String responsible = resultSet.getString("працівник.ПІБ");

            String bookingPerInfo = String.format(
                    "ID бронювання: %d\nСтатус: %s\nМешканець: %s\nНомер кімнати: %d\nДата початку: %s\nДата кінця: %s\nТермін проживання: %s\nВідповідальний: %s\n---------------------------",
                    id, status, client, roomNumber, startDateResult, endDateResult, bookingPeriod, responsible
                );

            bookingActive.add(bookingPerInfo);
        }
        
    	
    }catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return bookingActive;
    }

    private void displayBookingActiveData(String data) {
        JFrame frame = new JFrame("Інформація про активні бронювання");
        JTextArea textArea = new JTextArea(data);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14)); 
        textArea.setBackground(new Color(240, 240, 240));
        textArea.setForeground(Color.BLACK); 
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);
        frame.setSize(400, 600);
        frame.setVisible(true);;
    }
    
    public List<String> getBookingPerData(String startDate, String endDate) {
        List<String> bookingPerData = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            String query = String.format("SELECT * FROM бронювання JOIN працівник ON бронювання.Відповідальний_id = працівник.id_працівника " +
            "JOIN клієнт ON бронювання.Мешканець_id = клієнт.id_клієнта "+
            "JOIN дата_бронювання ON бронювання.Термін_проживання_id = дата_бронювання.id_дати_бронювання "+
            "WHERE дата_бронювання.Дата_початку BETWEEN '%s' AND '%s'", startDate, endDate);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
       
            while (resultSet.next()) {
                int id = resultSet.getInt("id_бронювання");
                String status = resultSet.getString("Статус");
                String client = resultSet.getString("клієнт.ПІБ");
                int roomNumber = resultSet.getInt("Кімната_id");
                String startDateResult = resultSet.getString("Дата_початку");
                String endDateResult = resultSet.getString("Дата_кінця");
                String bookingPeriod = resultSet.getString("Термін_проживання");
                String responsible = resultSet.getString("працівник.ПІБ");

                String bookingPerInfo = String.format(
                        "ID бронювання: %d\nСтатус: %s\nМешканець: %s\nНомер кімнати: %d\nДата початку: %s\nДата кінця: %s\nТермін проживання: %s\nВідповідальний: %s\n---------------------------",
                        id, status, client, roomNumber, startDateResult, endDateResult, bookingPeriod, responsible
                    );

                bookingPerData.add(bookingPerInfo);
            }
            

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return bookingPerData;
    }

    private void displayBookingPerData(String data) {
        JFrame frame = new JFrame("Бронювання за період");
        JTextArea textArea = new JTextArea(data);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14)); 
        textArea.setBackground(new Color(240, 240, 240));
        textArea.setForeground(Color.BLACK); 
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);
        frame.setSize(400, 600);
        frame.setVisible(true);
    }
    
    public List<String> getPaymentPerMonth(String month, String year) {
        List<String> paymentPerMonth = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            String query = "SELECT клієнт.ПІБ, COUNT(*) AS Кількість_платежів FROM платіж " +
                           "JOIN клієнт ON платіж.Відправник_id = клієнт.id_клієнта " +
                           "WHERE MONTH(Дата) = " + month + " AND YEAR(Дата) = " + year +
                           " GROUP BY клієнт.ПІБ";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String client = resultSet.getString("ПІБ");
                int paymentCount = resultSet.getInt("Кількість_платежів");

                String paymentInfo = String.format(
                        "Клієнт: %s\nКількість платежів: %d\n---------------------------",
                        client, paymentCount
                );

                paymentPerMonth.add(paymentInfo);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return paymentPerMonth;
    }

    private void displayPaymentPerMonth(String data) {
        JFrame frame = new JFrame("Платежі за певний місяць");
        JTextArea textArea = new JTextArea(data);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14)); 
        textArea.setBackground(new Color(240, 240, 240));
        textArea.setForeground(Color.BLACK); 
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);
        frame.setSize(400, 600);
        frame.setVisible(true);
    }
    
    public List<String> getHotelsWithMinRoomPrice() {
        List<String> hotelsWithMinPrice = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            String query = "SELECT Назва, Ціна " +
            			"FROM готель " +
                    	"JOIN кімната ON готель.id_готелю = кімната.Готель_id " +
                    	"WHERE кімната.Ціна <= ALL (" +
                        "SELECT Ціна " +
                        "FROM кімната " +
                        "WHERE кімната.Готель_id = готель.id_готелю)";

		    statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String hotelName = resultSet.getString("Назва");
                float price = resultSet.getFloat("Ціна");
                hotelsWithMinPrice.add(hotelName + " - " + price);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return hotelsWithMinPrice;
    }

    
    public int getLastClientId() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        int lastId = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            String query = "SELECT MAX(id_клієнта) AS last_id FROM клієнт";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                lastId = resultSet.getInt("last_id");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return lastId;
    }
    
    public int addClientData(String name, String phoneNumber, String dateBirth, String email) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            int newId = getLastClientId() + 1;
            
            String query = "INSERT INTO клієнт (id_клієнта, ПІБ, Номер_телефону, Дата_народження, Електрона_пошта) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, newId);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, phoneNumber);
            preparedStatement.setString(4, dateBirth);
            preparedStatement.setString(5, email);

            rowsAffected = preparedStatement.executeUpdate();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return rowsAffected;
    }
    
    public int addStaffData(int id, String name, String phoneNumber, String dateBirth, String dateStart, String position, int hotel_id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            String query = "INSERT INTO працівник (id_працівника, ПІБ, Номер_телефону, Дата_народження, Дата_початку_роботи, Посада, Готель_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, phoneNumber);
            preparedStatement.setString(4, dateBirth);
            preparedStatement.setString(5, dateStart);
            preparedStatement.setString(6, position);
            preparedStatement.setInt(7, hotel_id);

            rowsAffected = preparedStatement.executeUpdate();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return rowsAffected;
    }

    public int addDateBookingData(String startDate, String endDate) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            String bookingQuery = "INSERT INTO дата_бронювання (Дата_початку, Дата_кінця) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(bookingQuery);
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return rowsAffected;
    }

    public int addBookingData(String status, int roomId, int durationStayId, int addSerId, int clientId, int responsibleId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            String bookingQuery = "INSERT INTO бронювання (Статус, Кімната_id, Термін_проживання_id, Додаткові_послуги_id, Мешканець_id, Відповідальний_id) VALUES (?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(bookingQuery);
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, roomId);
            preparedStatement.setInt(3, durationStayId);
            preparedStatement.setInt(4, addSerId);
            preparedStatement.setInt(5, clientId);
            preparedStatement.setInt(6, responsibleId);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return rowsAffected;
    }

    public int addPaymentData(int clientId, int bookingId, float amount, String date, String status) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            String paymentQuery = "INSERT INTO платіж (Відправник_id, Бронювання_id, Сума, Дата, Статус) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(paymentQuery);
            preparedStatement.setInt(1, clientId);
            preparedStatement.setInt(2, bookingId);
            preparedStatement.setFloat(3, amount);
            preparedStatement.setString(4, date);
            preparedStatement.setString(5, status);

            rowsAffected = preparedStatement.executeUpdate();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return rowsAffected;
    }
    
    public int editClientData(int clientId, String name, String phoneNumber, String dateBirth, String email) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            String query = "UPDATE клієнт SET ПІБ = ?, Номер_телефону = ?, Дата_народження = ?, Електрона_пошта = ? WHERE id_клієнта = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, phoneNumber);
            preparedStatement.setString(3, dateBirth);
            preparedStatement.setString(4, email);
            preparedStatement.setInt(5, clientId);

            rowsAffected = preparedStatement.executeUpdate();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return rowsAffected;
    }

    public int editBookingStatus(int bookingId, String newStatus) {
        String query = "UPDATE бронювання SET Статус = ? WHERE id_бронювання = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, bookingId);
            
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    public int editPaymentStatus(int paymentId, String newStatus) {
        String query = "UPDATE платіж SET Статус = ? WHERE id_платежу = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, paymentId);
            
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    public int editRoomStatus(int roomId, String newStatus) {
        String query = "UPDATE кімната SET Статус = ? WHERE id_кімнати = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, roomId);
            
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    public int deleteStaffData(int staffId) {
        String query = "DELETE FROM працівник WHERE id_працівника = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, staffId);
            
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    public int deleteBooking(int bookingId) {
        String query = "DELETE FROM бронювання WHERE id_бронювання = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, bookingId);
            
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(HotelManagementApp.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createConnection();
                new HotelManagementApp();
            }
        });
        
    }
    class User {
        private String username;
        private UserRole role;

        public User(String username, UserRole role) {
            this.username = username;
            this.role = role;
        }

        public String getUsername() {
            return username;
        }

        public UserRole getRole() {
            return role;
        }
    }

    enum UserRole {
        ADMIN,
        USER
    }

}
