
    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.sql.*;
    import java.text.SimpleDateFormat;

    public class disease_crop {
        private JFrame frame;
        private JComboBox<String> tableComboBox;
        private JButton insertButton;
        private JButton updateButton;
        private JButton deleteButton;
        private JButton displayButton;
        private JButton clearButton;
        private JButton analyzeButton;
        
        private JTextArea outputTextArea;

        private Connection connection;

        public disease_crop() {
            try {
                // Establish the database connection
                connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","sreeram","vasavi");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: Failed to connect to the database.", "Database Connection Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        public void start() {
            frame = new JFrame("Disease Severity Tracker In Soyabean Crop");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setFrameSizeWithMargin(20);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new FlowLayout());
            
            outputTextArea = new JTextArea();
            outputTextArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(outputTextArea);
            scrollPane.setBounds(20, 125, 560, 300);
            frame.add(scrollPane);
            
            clearButton = new JButton("Clear");
            clearButton.setBounds(260, 80, 80, 30);
            analyzeButton = new JButton("Analyze");
            analyzeButton.setBounds(170,80,80,30);
            displayButton= new JButton("Display");
            displayButton.setBounds(80,80,80,30);
            frame.add(clearButton);
            frame.add(analyzeButton);
            frame.add(displayButton);

            JLabel tableLabel = new JLabel("Select a table:");
            tableComboBox = new JComboBox<>();
            tableComboBox.addItem("crops");
            tableComboBox.addItem("diseases");
            tableComboBox.addItem("disease_severity");
            tableComboBox.addItem("fields");
            tableComboBox.addItem("field_crops");

            insertButton = new JButton("Submit");
            updateButton = new JButton("Modify");
            deleteButton = new JButton("Delete");
            

            mainPanel.add(tableLabel);
            mainPanel.add(tableComboBox);
            mainPanel.add(insertButton);
            mainPanel.add(updateButton);
            mainPanel.add(deleteButton);

            insertButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String tableName = (String) tableComboBox.getSelectedItem();
                    if (tableName != null && !tableName.isEmpty()) {
                        if (tableName.equals("crops")) {
                            insertCropsRecord();
                        } else if (tableName.equals("diseases")) {
                            insertDiseasesRecord();
                        } else if (tableName.equals("fields")) {
                        	insertFieldsRecord();
                        } else if(tableName.equals("field_crops")) {
                        	insertFieldCropRecord();
                        } else if(tableName.equals("disease_severity")) {
                        	insertDiseaseSeverityRecord();
                        }
                        	else {
                            insertRecord(tableName);
                        }
                    }
                }
            });

            updateButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String tableName = (String) tableComboBox.getSelectedItem();
                    if (tableName != null && !tableName.isEmpty()) {
                        if (tableName.equals("crops")) {
                            updateCropsRecord();
                        } else if (tableName.equals("diseases")) {
                            updateDiseasesRecord();
                        }
                        else if(tableName.equals("fields")) {
                        	updateFieldsRecord();
                        } else if(tableName.equals("field_crops")) {
                        	updateFieldCropRecord();
                        } else if(tableName.equals("disease_severity")) {
                        	updateDiseaseSeverityRecord();
                        }
                        else {
                            updateRecord(tableName);
                        }
                    }
                }
            });

            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String tableName = (String) tableComboBox.getSelectedItem();
                    if (tableName != null && !tableName.isEmpty()) {
                        if (tableName.equals("crops")) {
                            deleteCropsRecord();
                        } else if (tableName.equals("diseases")) {
                            deleteDiseasesRecord();
                        } else if(tableName.equals("fields")) {
                        	deleteFieldsRecord();
                        } else if(tableName.equals("field_crops")) {
                        	deleteFieldCropRecord();
                        } else if(tableName.equals("disease_severity")) {
                        	deleteDiseaseSeverityRecord();
                        }
                          else {
                            deleteRecord(tableName);
                        }
                    }
                }
            });
            
            displayButton.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent e) {
            		String tableName = (String) tableComboBox.getSelectedItem();
            		if(tableName !=null && !tableName.isEmpty()) {
            			if(tableName.equals("crops")) {
            				displayCrops();
            			} else if(tableName.equals("diseases")) {
            			    displayDiseases();
            			} else if(tableName.equals("disease_severity")) {
            				displayDiseaseSeverity();
            			} else if(tableName.equals("fields")) {
            				displayFields();
            			} else if(tableName.equals("field_crops")) {
            				displayFieldCrops();
            			} else {
            				displayRecord(tableName);
            			}
            		}
            	}
            });
            
            clearButton.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent e) {
            		outputTextArea.setText(null);	
            	}
            });
            
            analyzeButton.addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent e) {
            			
            		    outputTextArea.setText(null); // Clear the text area

            		    try {
            		        Statement statement = connection.createStatement();
            		        ResultSet resultSet = statement.executeQuery("select ds.crop_id,c.crop_name,c.crop_planted_date,ds.disease_id,d.disease_name,ds.severity_id,ds.dat,ds.severity_score from crops c,diseases d,disease_severity ds where c.crop_id=ds.crop_id and ds.disease_id=d.disease_id");
            		        StringBuilder sb = new StringBuilder();		
            		        while (resultSet.next()) {
            		    
            		            int cropId = resultSet.getInt("crop_id");
            		            String cropName = resultSet.getString("crop_name");
            		            Date cropdate=resultSet.getDate("crop_planted_date");
            		            int diseaseId = resultSet.getInt("disease_id");
            		            String diseaseName = resultSet.getString("disease_name");
            		            int severityId = resultSet.getInt("severity_id");
            		            Date date = resultSet.getDate("dat");
            		            double severityScore = resultSet.getDouble("severity_score");
            		            
            		            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            	                String formattedDate = dateFormat.format(date);
            	                
            	                SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MMM-yyyy");
            	                String formatteddate = dateFormat1.format(cropdate);
            	                
            		            
            		            sb.append(" Crop ID: ").append(cropId).append("\n");
            		            sb.append(" Crop Name: ").append(cropName).append("\n");
            		            sb.append(" Crop Planted Date: ").append(formatteddate).append("\n");
            		            sb.append(" Disease ID: ").append(diseaseId).append("\n");
            		            sb.append(" Disease Name: ").append(diseaseName).append("\n");
            		            sb.append(" Severity ID: ").append(severityId).append("\n");
            		            sb.append(" Date: ").append(formattedDate).append("\n");
            		            sb.append(" Severity Score: ").append(severityScore).append("\n");
            		            sb.append("\n");
            		            // Retrieve other columns as needed
            		            
            		        }
            		        
            		        outputTextArea.setText(sb.toString());
            		        outputTextArea.append("------------------------------------------------------------------------------------------------------------------------------------\nTHE DETAILS OF CROPS WITH DISEASES ARE DISPLAYED ABOVE.\n");

            		        resultSet.close();
            		        statement.close();
            		    } catch (SQLException ex) {
            		        ex.printStackTrace();
            		        JOptionPane.showMessageDialog(null, "Error: Failed to analyze disease severity data.", "Analysis Error", JOptionPane.ERROR_MESSAGE);
            		    }
            		}

            	});

            frame.add(mainPanel);
            frame.setLocationRelativeTo(null);
            frame.setSize(1000,500);
            frame.setVisible(true);
            
        }

        private void insertRecord(String tableName) {
            // Implement the insert operation for the selected table
            // Use the 'connection' object to execute the SQL statement
            JOptionPane.showMessageDialog(null, "Insert operation for table " + tableName + " selected.");
        }
        private void insertCropsRecord() {
            // Prompt for input for columns of crops table
            String cropIdStr = JOptionPane.showInputDialog(null, "Enter Crop ID:");
            String cropName = JOptionPane.showInputDialog(null, "Enter Crop Name:");
            String cropPlantedDate = JOptionPane.showInputDialog(null, "Enter Crop Planted Date (yyyy-MM-dd):");

            try {
            
            	int cropId = Integer.parseInt(cropIdStr);
                // Prepare the SQL statement
                String sql = "INSERT INTO crops (crop_id, crop_name, crop_planted_date) VALUES (?, ?, TO_DATE(?, 'yyyy-MM-dd'))";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, cropId);
                statement.setString(2, cropName);
                statement.setString(3, cropPlantedDate);

                // Execute the SQL statement
                int rowsAffected = statement.executeUpdate();

                // Check the number of rows affected to determine if the insert was successful
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Crop record inserted successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to insert crop record.", "Insert Error", JOptionPane.ERROR_MESSAGE);
                }

                // Close the statement
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: Failed to insert crop record.", "Insert Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Invalid Crop ID. Please enter a valid integer.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void insertDiseasesRecord() {
            // Prompt for input for columns of diseases table
            String diseaseIdStr = JOptionPane.showInputDialog(null, "Enter Disease ID:");
            String diseaseName = JOptionPane.showInputDialog(null, "Enter Disease Name:");

            try {
                // Parse the disease ID as an integer
                int diseaseId = Integer.parseInt(diseaseIdStr);

                // Prepare the SQL statement
                String sql = "INSERT INTO diseases (disease_id, disease_name) VALUES (?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, diseaseId);
                statement.setString(2, diseaseName);

                // Execute the SQL statement
                int rowsAffected = statement.executeUpdate();

                // Check the number of rows affected to determine if the insert was successful
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Disease record inserted successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to insert disease record.", "Insert Error", JOptionPane.ERROR_MESSAGE);
                }

                // Close the statement
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: Failed to insert disease record.", "Insert Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Invalid Disease ID. Please enter a valid integer.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }

            private void insertFieldsRecord() {
                // Prompt for input for columns of fields table
                String fieldIdStr = JOptionPane.showInputDialog(null, "Enter Field ID:");
                String fieldName = JOptionPane.showInputDialog(null, "Enter Field Name:");
                String fieldSize = JOptionPane.showInputDialog(null, "Enter Field size:");

                try {
                    // Parse the field ID as an integer
                    int fieldId = Integer.parseInt(fieldIdStr);
                    int fieldsize=Integer.parseInt(fieldSize);
                    // Prepare the SQL statement
                    String sql = "INSERT INTO fields (field_id, field_name, field_size) VALUES (?, ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setInt(1, fieldId);
                    statement.setString(2, fieldName);
                    statement.setInt(3, fieldsize);

                    // Execute the SQL statement
                    int rowsAffected = statement.executeUpdate();

                    // Check the number of rows affected to determine if the insert was successful
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Field record inserted successfully.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to insert field record.", "Insert Error", JOptionPane.ERROR_MESSAGE);
                    }

                    // Close the statement
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error: Failed to insert field record.", "Insert Error", JOptionPane.ERROR_MESSAGE);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Error: Invalid Field ID. Please enter a valid integer.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
  
            private void insertFieldCropRecord() {
                // Prompt for input for columns of field_crops table
                String fieldCropIdStr = JOptionPane.showInputDialog(null, "Enter Field Crop ID:");
                String fieldIdStr = JOptionPane.showInputDialog(null, "Enter Field ID:");
                String cropIdStr = JOptionPane.showInputDialog(null, "Enter Crop ID:");
                String datePlanted = JOptionPane.showInputDialog(null, "Enter Date Planted (YYYY-MM-DD):");

                try {
                    // Parse the input values
                    int fieldCropId = Integer.parseInt(fieldCropIdStr);
                    int fieldId = Integer.parseInt(fieldIdStr);
                    int cropId = Integer.parseInt(cropIdStr);

                    // Prepare the SQL statement
                    String sql = "INSERT INTO field_crops (field_crop_id, field_id, crop_id, date_planted) VALUES (?, ?, ?, TO_DATE(?,'yyyy-MM-dd'))";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setInt(1, fieldCropId);
                    statement.setInt(2, fieldId);
                    statement.setInt(3, cropId);
                    statement.setString(4, datePlanted);

                    // Execute the SQL statement
                    int rowsAffected = statement.executeUpdate();

                    // Check the number of rows affected to determine if the insert was successful
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Field crop record inserted successfully.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to insert field crop record.", "Insert Error", JOptionPane.ERROR_MESSAGE);
                    }

                    // Close the statement
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error: Failed to insert field crop record.", "Insert Error", JOptionPane.ERROR_MESSAGE);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Error: Invalid input. Please enter valid integer values.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
            private void insertDiseaseSeverityRecord() {
                // Prompt for input for columns of disease_severity table
                String severityIdStr = JOptionPane.showInputDialog(null, "Enter Severity ID:");
                String cropIdStr = JOptionPane.showInputDialog(null, "Enter Crop ID:");
                String diseaseIdStr = JOptionPane.showInputDialog(null, "Enter Disease ID:");
                String dateStr = JOptionPane.showInputDialog(null, "Enter Date (YYYY-MM-DD):");
                String severityScoreStr = JOptionPane.showInputDialog(null, "Enter Severity Score:");

                try {
                    // Parse the input values
                    int severityId = Integer.parseInt(severityIdStr);
                    int cropId = Integer.parseInt(cropIdStr);
                    int diseaseId = Integer.parseInt(diseaseIdStr);
                    int severityScore = Integer.parseInt(severityScoreStr);

                    // Prepare the SQL statement
                    String sql = "INSERT INTO disease_severity (severity_id, crop_id, disease_id, dat, severity_score) VALUES (?, ?, ?, TO_DATE(?,'yyyy-MM-dd'), ?)";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setInt(1, severityId);
                    statement.setInt(2, cropId);
                    statement.setInt(3, diseaseId);
                    statement.setString(4, dateStr);
                    statement.setInt(5, severityScore);

                    // Execute the SQL statement
                    int rowsAffected = statement.executeUpdate();

                    // Check the number of rows affected to determine if the insert was successful
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Disease severity record inserted successfully.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to insert disease severity record.", "Insert Error", JOptionPane.ERROR_MESSAGE);
                    }

                    // Close the statement
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error: Failed to insert disease severity record.", "Insert Error", JOptionPane.ERROR_MESSAGE);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Error: Invalid input. Please enter valid integer and numeric values.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                } 
                
            }
    
            
    private void updateRecord(String tableName) {
        // Implement the update operation for the selected table
        // Use the 'connection' object to execute the SQL statement
        JOptionPane.showMessageDialog(null, "Update operation for table " + tableName + " selected.");
    }

    private void updateCropsRecord() {
        // Prompt for input for columns of crops table
        String cropIdStr = JOptionPane.showInputDialog(null, "Enter Crop ID to update:");
        String cropName = JOptionPane.showInputDialog(null, "Enter Updated Crop Name:");
        String cropPlantedDate = JOptionPane.showInputDialog(null, "Enter Updated Crop Planted Date (yyyy-MM-dd):");

        try {
            // Parse the crop ID as an integer
            int cropId = Integer.parseInt(cropIdStr);

            // Prepare the SQL statement
            String sql = "UPDATE crops SET crop_name = ?, crop_planted_date = TO_DATE(?, 'yyyy-MM-dd') WHERE crop_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, cropName);
            statement.setString(2, cropPlantedDate);
            statement.setInt(3, cropId);

            // Execute the SQL statement
            int rowsAffected = statement.executeUpdate();

            // Check the number of rows affected to determine if the update was successful
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Crop record updated successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update crop record.", "Update Error", JOptionPane.ERROR_MESSAGE);
            }

            // Close the statement
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Failed to update crop record.", "Update Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Invalid Crop ID. Please enter a valid integer.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateDiseasesRecord() {
        // Prompt for input for columns of diseases table
        String diseaseIdStr = JOptionPane.showInputDialog(null, "Enter Disease ID to update:");
        String diseaseName = JOptionPane.showInputDialog(null, "Enter Updated Disease Name:");

        try {
            // Parse the disease ID as an integer
            int diseaseId = Integer.parseInt(diseaseIdStr);

            // Prepare the SQL statement
            String sql = "UPDATE diseases SET disease_name = ? WHERE disease_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, diseaseName);
            statement.setInt(2, diseaseId);

            // Execute the SQL statement
            int rowsAffected = statement.executeUpdate();

            // Check the number of rows affected to determine if the update was successful
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Disease record updated successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update disease record.", "Update Error", JOptionPane.ERROR_MESSAGE);
            }

            // Close the statement
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Failed to update disease record.", "Update Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Invalid Disease ID. Please enter a valid integer.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateFieldsRecord() {
        // Prompt for input for columns of fields table
        String fieldIdStr = JOptionPane.showInputDialog(null, "Enter Field ID to update:");
        String fieldName = JOptionPane.showInputDialog(null, "Enter updated Field Name:");
        String fieldSize = JOptionPane.showInputDialog(null, "Enter updated Field Size:");

        try {
            // Parse the field ID as an integer
            int fieldId = Integer.parseInt(fieldIdStr);
            int fieldsize = Integer.parseInt(fieldSize);

            // Prepare the SQL statement
            String sql = "UPDATE fields SET field_name = ?, field_size = ? WHERE field_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, fieldName);
            statement.setInt(2, fieldsize);
            statement.setInt(3, fieldId);

            // Execute the SQL statement
            int rowsAffected = statement.executeUpdate();

            // Check the number of rows affected to determine if the update was successful
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Field record updated successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update field record.", "Update Error", JOptionPane.ERROR_MESSAGE);
            }

            // Close the statement
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Failed to update field record.", "Update Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Invalid Field ID. Please enter a valid integer.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateFieldCropRecord() {
        // Prompt for input to identify the record to update
        String fieldCropIdStr = JOptionPane.showInputDialog(null, "Enter Field Crop ID:");

        try {
            // Parse the input value
            int fieldCropId = Integer.parseInt(fieldCropIdStr);

            // Prompt for input to update columns of field_crops table
            String fieldIdStr = JOptionPane.showInputDialog(null, "Enter Field ID:");
            String cropIdStr = JOptionPane.showInputDialog(null, "Enter Crop ID:");
            String datePlanted = JOptionPane.showInputDialog(null, "Enter Date Planted (YYYY-MM-DD):");

            // Prepare the SQL statement
            String sql = "UPDATE field_crops SET field_id = ?, crop_id = ?, date_planted = TO_DATE(?,'yyyy-MM-dd') WHERE field_crop_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(fieldIdStr));
            statement.setInt(2, Integer.parseInt(cropIdStr));
            statement.setString(3, datePlanted);
            statement.setInt(4, fieldCropId);

            // Execute the SQL statement
            int rowsAffected = statement.executeUpdate();

            // Check the number of rows affected to determine if the update was successful
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Field crop record updated successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update field crop record.", "Update Error", JOptionPane.ERROR_MESSAGE);
            }

            // Close the statement
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Failed to update field crop record.", "Update Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Invalid input. Please enter valid integer and date values.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateDiseaseSeverityRecord() {
        // Prompt for input for the columns to update
        String severityIdStr = JOptionPane.showInputDialog(null, "Enter Severity ID:");
        String cropIdStr = JOptionPane.showInputDialog(null, "Enter Crop ID:");
        String diseaseIdStr = JOptionPane.showInputDialog(null, "Enter Disease ID:");
        String dateStr = JOptionPane.showInputDialog(null, "Enter Date (YYYY-MM-DD):");
        String severityScoreStr = JOptionPane.showInputDialog(null, "Enter Severity Score:");

        try {
            // Parse the input values
            int severityId = Integer.parseInt(severityIdStr);
            int cropId = Integer.parseInt(cropIdStr);
            int diseaseId = Integer.parseInt(diseaseIdStr);
            int severityScore = Integer.parseInt(severityScoreStr);

            // Prepare the SQL statement
            String sql = "UPDATE disease_severity SET crop_id = ?, disease_id = ?, dat = TO_DATE(?,'yyyy-MM-dd'), severity_score = ? WHERE severity_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, cropId);
            statement.setInt(2, diseaseId);
            statement.setString(3, dateStr);
            statement.setInt(4, severityScore);
            statement.setInt(5, severityId);

            // Execute the SQL statement
            int rowsAffected = statement.executeUpdate();

            // Check the number of rows affected to determine if the update was successful
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Disease severity record updated successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update disease severity record.", "Update Error", JOptionPane.ERROR_MESSAGE);
            }

            // Close the statement
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Failed to update disease severity record.", "Update Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Invalid input. Please enter valid integer and numeric values.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } 
    }

    
    
    private void deleteRecord(String tableName) {
        // Implement the delete operation for the selected table
        // Use the 'connection' object to execute the SQL statement
        JOptionPane.showMessageDialog(null, "Delete operation for table " + tableName + " selected.");
    }

    private void deleteDiseasesRecord() {
        // Prompt for input for columns of diseases table
        String diseaseIdStr = JOptionPane.showInputDialog(null, "Enter Disease ID to delete:");

        try {
            // Parse the disease ID as an integer
            int diseaseId = Integer.parseInt(diseaseIdStr);

            // Prepare the SQL statement
            String sql = "DELETE FROM diseases WHERE disease_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, diseaseId);

            // Execute the SQL statement
            int rowsAffected = statement.executeUpdate();

            // Check the number of rows affected to determine if the delete was successful
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Disease record deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete disease record.", "Delete Error", JOptionPane.ERROR_MESSAGE);
            }

            // Close the statement
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Failed to delete disease record.", "Delete Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Invalid Disease ID. Please enter a valid integer.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
  
    private void deleteCropsRecord() {
        // Prompt for input for columns of crops table
        String cropIdStr = JOptionPane.showInputDialog(null, "Enter Crop ID to delete:");

        try {
            // Parse the crop ID as an integer
            int cropId = Integer.parseInt(cropIdStr);

            // Prepare the SQL statement
            String sql = "DELETE FROM crops WHERE crop_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, cropId);

            // Execute the SQL statement
            int rowsAffected = statement.executeUpdate();

            // Check the number of rows affected to determine if the delete was successful
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Crop record deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete crop record.", "Delete Error", JOptionPane.ERROR_MESSAGE);
            }

            // Close the statement
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Failed to delete crop record.", "Delete Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Invalid Crop ID. Please enter a valid integer.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteFieldsRecord() {
        // Prompt for input for the field ID to delete
        String fieldIdStr = JOptionPane.showInputDialog(null, "Enter Field ID to delete:");

        try {
            // Parse the field ID as an integer
            int fieldId = Integer.parseInt(fieldIdStr);

            // Prepare the SQL statement
            String sql = "DELETE FROM fields WHERE field_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, fieldId);

            // Execute the SQL statement
            int rowsAffected = statement.executeUpdate();

            // Check the number of rows affected to determine if the delete was successful
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Field record deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete field record.", "Delete Error", JOptionPane.ERROR_MESSAGE);
            }

            // Close the statement
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Failed to delete field record.", "Delete Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Invalid Field ID. Please enter a valid integer.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteFieldCropRecord() {
        // Prompt for input to identify the record to delete
        String fieldCropIdStr = JOptionPane.showInputDialog(null, "Enter Field Crop ID:");

        try {
            // Parse the input value
            int fieldCropId = Integer.parseInt(fieldCropIdStr);

            // Prepare the SQL statement
            String sql = "DELETE FROM field_crops WHERE field_crop_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, fieldCropId);

            // Execute the SQL statement
            int rowsAffected = statement.executeUpdate();

            // Check the number of rows affected to determine if the delete was successful
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Field crop record deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete field crop record.", "Delete Error", JOptionPane.ERROR_MESSAGE);
            }

            // Close the statement
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Failed to delete field crop record.", "Delete Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Invalid input. Please enter a valid integer.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteDiseaseSeverityRecord() {
        // Prompt for input for the severity ID of the record to delete
        String severityIdStr = JOptionPane.showInputDialog(null, "Enter Severity ID:");

        try {
            // Parse the severity ID
            int severityId = Integer.parseInt(severityIdStr);

            // Prepare the SQL statement
            String sql = "DELETE FROM disease_severity WHERE severity_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, severityId);

            // Execute the SQL statement
            int rowsAffected = statement.executeUpdate();

            // Check the number of rows affected to determine if the delete was successful
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Disease severity record deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete disease severity record.", "Delete Error", JOptionPane.ERROR_MESSAGE);
            }

            // Close the statement
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Failed to delete disease severity record.", "Delete Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Invalid Severity ID. Please enter a valid integer.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayRecord(String tableName) {
    	JOptionPane.showMessageDialog(null,"Display operation for " +tableName+ "selected.");
    }
   
    
    private void displayCrops() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM crops");

            StringBuilder sb = new StringBuilder();
            while (resultSet.next()) {
                int cropId = resultSet.getInt("crop_id");
                String cropName = resultSet.getString("crop_name");
                Date cropDate = resultSet.getDate("crop_planted_date");
                // Retrieve other columns as needed

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = dateFormat.format(cropDate);

                sb.append("Crop ID: ").append(cropId).append("\n");
                sb.append("Crop Name: ").append(cropName).append("\n");
                sb.append("Crop Planted Date: ").append(formattedDate).append("\n");
                sb.append("---------------------------\n");
            }

            outputTextArea.setText(sb.toString());

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Failed to retrieve crops data.", "Data Retrieval Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayDiseases() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM diseases");

            StringBuilder sb = new StringBuilder();
            while (resultSet.next()) {
                int diseaseId = resultSet.getInt("disease_id");
                String diseaseName = resultSet.getString("disease_name");
                // Retrieve other columns as needed

                sb.append("Disease ID: ").append(diseaseId).append("\n");
                sb.append("Disease Name: ").append(diseaseName).append("\n");
                sb.append("---------------------------\n");
            }

            outputTextArea.setText(sb.toString());

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Failed to retrieve diseases data.", "Data Retrieval Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayDiseaseSeverity() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM disease_severity");

            StringBuilder sb = new StringBuilder();
            while (resultSet.next()) {
                int severityId = resultSet.getInt("severity_id");
                int cropId = resultSet.getInt("crop_id");
                int diseaseId = resultSet.getInt("disease_id");
                Date date = resultSet.getDate("dat");
                double severityScore = resultSet.getDouble("severity_score");
                // Retrieve other columns as needed
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = dateFormat.format(date);

                sb.append("Severity ID: ").append(severityId).append("\n");
                sb.append("Crop ID: ").append(cropId).append("\n");
                sb.append("Disease ID: ").append(diseaseId).append("\n");
                sb.append("Date: ").append(formattedDate).append("\n");
                sb.append("Severity Score: ").append(severityScore).append("\n");
                sb.append("---------------------------\n");
            }

            outputTextArea.setText(sb.toString());

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Failed to retrieve disease severity data.", "Data Retrieval Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayFields() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM fields");

            StringBuilder sb = new StringBuilder();
            while (resultSet.next()) {
                int fieldId = resultSet.getInt("field_id");
                String fieldName = resultSet.getString("field_name");
                int fieldSize = resultSet.getInt("field_size");
                // Retrieve other columns as needed

                sb.append("Field ID: ").append(fieldId).append("\n");
                sb.append("Field Name: ").append(fieldName).append("\n");
                sb.append("Field Size: ").append(fieldSize).append("\n");
                sb.append("---------------------------\n");
            }

            outputTextArea.setText(sb.toString());

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Failed to retrieve fields data.", "Data Retrieval Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayFieldCrops() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM field_crops");

            StringBuilder sb = new StringBuilder();
            while (resultSet.next()) {
                int fieldCropId = resultSet.getInt("field_crop_id");
                int fieldId = resultSet.getInt("field_id");
                int cropId = resultSet.getInt("crop_id");
                Date datePlanted = resultSet.getDate("date_planted");
                // Retrieve other columns as needed
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = dateFormat.format(datePlanted);

                sb.append("Field Crop ID: ").append(fieldCropId).append("\n");
                sb.append("Field ID: ").append(fieldId).append("\n");
                sb.append("Crop ID: ").append(cropId).append("\n");
                sb.append("Date Planted: ").append(formattedDate).append("\n");
                sb.append("---------------------------\n");
            }

            outputTextArea.setText(sb.toString());

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Failed to retrieve field crops data.", "Data Retrieval Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void setFrameSizeWithMargin(int marginPercentage) {
        // Get the screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Calculate the frame size with the specified margin
        int marginWidth = (screenWidth * marginPercentage) / 100;
        int marginHeight = (screenHeight * marginPercentage) / 100;
        int frameWidth = screenWidth - (2 * marginWidth);
        int frameHeight = screenHeight - (2 * marginHeight);

        // Set the frame size
        frame.setSize(frameWidth, frameHeight);
    }

    public static void main(String[] args) {
        disease_crop app = new disease_crop();
        app.start();
    }
}
