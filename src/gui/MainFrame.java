package gui;

import entity.Product;
import fileActions.FileActions;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static entity.Product.ProductNameComparator;

public class MainFrame extends JFrame {
    public Border blackline = BorderFactory.createLineBorder(Color.black);

    public HashMap<Product, Integer> cart=new HashMap<Product, Integer>();

    public JPanel makeTitlePanel() {
        JPanel shopNamePanel = new JPanel(new BorderLayout());
        JLabel shopNameLabel = new JLabel("MyShop", SwingConstants.CENTER);
        shopNameLabel.setVerticalAlignment(SwingConstants.CENTER);
        shopNameLabel.setBorder(blackline);
        shopNamePanel.setSize(600, 200);
        shopNameLabel.setFont(new Font("Serif", Font.PLAIN, 30));
        shopNamePanel.add(shopNameLabel);

        return shopNamePanel;
    }

    public JPanel makeWelcomePanel() {
        JPanel welcomePanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome! Please, select a category.");
        welcomeLabel.setFont(new Font("Serif", Font.PLAIN, 30));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel welcomeLabelPanel = new JPanel(new BorderLayout());
        welcomeLabelPanel.setPreferredSize(new Dimension(700, 300));
        welcomeLabelPanel.add(welcomeLabel, BorderLayout.CENTER);
        welcomePanel.add(welcomeLabelPanel, BorderLayout.PAGE_START);
        return welcomePanel;
    }

    public JPanel makeProductBox(Product product) throws IOException {
        JPanel productBox = new JPanel(new BorderLayout());
        productBox.setPreferredSize(new Dimension(165, 200));

        // top
        JPanel top=new JPanel(new BorderLayout());
        JLabel productNameLabel = new JLabel(product.getName());
        productNameLabel.setHorizontalAlignment(JLabel.CENTER);

        BufferedImage myPicture = ImageIO.read(new File(product.getImagePath()));
        Image image = myPicture.getScaledInstance(120, 120, Image.SCALE_DEFAULT);
        JLabel pictureLabel = new JLabel(new ImageIcon(image));
        pictureLabel.setHorizontalAlignment(JLabel.CENTER);

        top.add(productNameLabel, BorderLayout.NORTH);
        top.add(pictureLabel, BorderLayout.SOUTH);

        // bottom
        JPanel bottom=new JPanel(new BorderLayout());
        JLabel priceLabel = new JLabel(String.format("%.02f", product.getPrice()) + " lv.");
        priceLabel.setHorizontalAlignment(JLabel.CENTER);


        // Shopping cart and quantity
        JPanel quantityAndCartPanel=new JPanel(new BorderLayout());

        BufferedImage shoppingCart = ImageIO.read(new File("images/icons8-shopping-cart-30.png"));
        Image shoppingCartImage = shoppingCart.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon icon=new ImageIcon(shoppingCartImage);
        JButton shoppingCartButton = new JButton(icon);
        shoppingCartButton.setOpaque(false);
        shoppingCartButton.setContentAreaFilled(false);
        shoppingCartButton.setBorderPainted(false);

        SpinnerModel model = new SpinnerNumberModel(1, 1, 25, 1);
        JSpinner productQuantitySpinner = new JSpinner(model);
        productQuantitySpinner.setPreferredSize(new Dimension(80, 30));

        shoppingCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cart.get(product)==null){
                    cart.put(product, (Integer) productQuantitySpinner.getValue());
                }else {
                    int newQuantity=cart.get(product) + (Integer) productQuantitySpinner.getValue();
                    cart.replace(product, newQuantity);
                }
                productQuantitySpinner.setValue(1);
                System.out.println(cart);
            }
        });

        quantityAndCartPanel.add(shoppingCartButton, BorderLayout.WEST);
        quantityAndCartPanel.add(productQuantitySpinner, BorderLayout.EAST);

        bottom.add(priceLabel, BorderLayout.NORTH);
        bottom.add(quantityAndCartPanel, BorderLayout.SOUTH);

        productBox.add(top, BorderLayout.NORTH);
        productBox.add(bottom, BorderLayout.SOUTH);

        productBox.setBorder(blackline);

        return productBox;
    }

    public JScrollPane makeProductsCatalog(ArrayList<Product> products, JPanel right) throws IOException {
        JPanel productsCatalog=new JPanel(new FlowLayout(FlowLayout.LEFT));
        productsCatalog.setPreferredSize(new Dimension(700, 500));
        for(Product product:products){
            JPanel productBox=makeProductBox(product);
            productsCatalog.add(productBox);
        }

        JPanel buttonsPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton sortByPriceBtn=new JButton("Sort by price");
        sortByPriceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Product[] products1= products.toArray(new Product[0]);
                    Arrays.sort(products1);
                    ArrayList<Product> list1 = new ArrayList<Product>(List.of(products1));
                    JScrollPane catalog=makeProductsCatalog(list1, right);
                    right.removeAll();
                    right.add(catalog);
                    right.repaint();
                    pack();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton sortByNameBtn=new JButton("Sort by name");
        sortByNameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Product[] products2= products.toArray(new Product[0]);
                Arrays.sort(products2, ProductNameComparator);
                ArrayList<Product> list1 = new ArrayList<Product>(List.of(products2));
                try {
                    JScrollPane catalog=makeProductsCatalog(list1, right);
                    right.removeAll();
                    right.add(catalog);
                    right.repaint();
                    pack();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });

        JPanel cartAndQuantityInfoPanel=new JPanel(new BorderLayout());
        JLabel quantityInfoLabel=new JLabel();
        quantityInfoLabel.setVisible(false);
        BufferedImage shoppingCart = ImageIO.read(new File("images/icons8-shopping-cart-30.png"));
        Image shoppingCartImage = shoppingCart.getScaledInstance(45, 45, Image.SCALE_DEFAULT);
        ImageIcon icon=new ImageIcon(shoppingCartImage);
        JButton seeCartBtn = new JButton(icon);
        seeCartBtn.setOpaque(false);
        seeCartBtn.setContentAreaFilled(false);
        seeCartBtn.setBorderPainted(false);
        seeCartBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                int quantity=0;
                for (Object key:cart.keySet()){
                    quantity+=cart.get(key);
                }
                quantityInfoLabel.setText("You have "+quantity+" item/s in your cart.");
                quantityInfoLabel.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                quantityInfoLabel.setVisible(false);
            }
        });
        MainFrame frame=this;
        seeCartBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog modelDialog = new JDialog(frame, "Cart items", Dialog.ModalityType.DOCUMENT_MODAL);
                modelDialog.setBounds(200, 200, 600, 550);
                Container dialogContainer = modelDialog.getContentPane();
                dialogContainer.setLayout(new FlowLayout());
                float finalPrice=0;

                // Table
                String[] columnNames = {"Product Name",
                        "Quantity",
                        "Price",
                        "Overall"};

                Object[][] data = new Object[cart.size()][4];
                int counter=0;
                for(Product product: cart.keySet()){
                    float overall=cart.get(product)*product.getPrice();
                    finalPrice+=overall;
                    Object[] productData={product.getName(), cart.get(product), product.getPrice(), overall};
                    data[counter]=productData;
                    counter+=1;
                }

                JTable productsTable=new JTable(data, columnNames);
                productsTable.setShowGrid(true);
                productsTable.setGridColor(Color.BLACK);
                productsTable.getTableHeader().setBorder(blackline);

                JScrollPane scrollPane = new JScrollPane(productsTable);
                scrollPane.setPreferredSize(new Dimension(550, 350));

                TableColumnModel model = productsTable.getColumnModel();
                TableColumn col = model.getColumn(1);

                col.setCellEditor(new MySpinnerEditor());

                // info label
                JLabel infoLabel=new JLabel("Double click on the quantity cell you want to edit.", SwingConstants.CENTER);
                infoLabel.setPreferredSize(new Dimension(550, 50));


                // final price label and save button panel
                JPanel priceAndSavePanel=new JPanel(new BorderLayout());
                JButton saveBtn=new JButton("Save changes");
                saveBtn.setPreferredSize(new Dimension(100, 30));
                saveBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        for (int i=0; i<cart.size(); i++){
                            String currentProductName= (String) productsTable.getValueAt(i, 0);
                            int currentProductQuantity= (int) productsTable.getValueAt(i, 1);

                            for (Product productFromCartMap:cart.keySet()){
                                if (productFromCartMap.getName().equals(currentProductName)){
                                    cart.replace(productFromCartMap, currentProductQuantity);
                                }
                            }

                        }
                        modelDialog.setVisible(false);
                        seeCartBtn.doClick();
                    }
                });
                JLabel finalPriceLabel=new JLabel("Overall "+String.format("%.02f", finalPrice)+" lv.", SwingConstants.RIGHT);

                priceAndSavePanel.add(saveBtn, BorderLayout.WEST);
                priceAndSavePanel.add(finalPriceLabel, BorderLayout.EAST);
                priceAndSavePanel.setPreferredSize(new Dimension(550, 50));

                // Buttons panel
                JPanel payButtonsPanel=new JPanel(new FlowLayout());
                JButton payByCardBtn=new JButton("Pay by card");
                payByCardBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        FileActions.makeInvoice(cart);
                        modelDialog.dispose();
                        cart.clear();
                    }
                });
                JButton payViaPayPal=new JButton("Pay via PayPal");
                payViaPayPal.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        FileActions.makeInvoice(cart);
                        modelDialog.dispose();
                        cart.clear();
                    }
                });
                payButtonsPanel.add(payByCardBtn);
                payButtonsPanel.add(payViaPayPal);
                payButtonsPanel.setPreferredSize(new Dimension(550, 100));

                dialogContainer.add(scrollPane);
                dialogContainer.add(infoLabel);
                dialogContainer.add(priceAndSavePanel);
                dialogContainer.add(payButtonsPanel);

                modelDialog.setVisible(true);
            }
        });

        cartAndQuantityInfoPanel.add(seeCartBtn, BorderLayout.NORTH);
        cartAndQuantityInfoPanel.add(quantityInfoLabel, BorderLayout.SOUTH);
        JPanel cartAndQuantityInfoPanel1=new JPanel(new FlowLayout(FlowLayout.CENTER));
        cartAndQuantityInfoPanel1.add(cartAndQuantityInfoPanel);

        buttonsPanel.add(sortByNameBtn);
        buttonsPanel.add(sortByPriceBtn);
        buttonsPanel.setPreferredSize(new Dimension(700, 50));

        productsCatalog.add(buttonsPanel);
        productsCatalog.add(cartAndQuantityInfoPanel1);

        JScrollPane scrollPane = new JScrollPane(productsCatalog);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        return scrollPane;
    }

    public MainFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(900, 900);
        this.setLocationRelativeTo(null);

        // shop name
        this.add(makeTitlePanel(), BorderLayout.NORTH);

        //Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel left = new JPanel(new BorderLayout());
        left.setPreferredSize(new Dimension(200, 700));
        left.setBorder(blackline);
        mainPanel.add(left, BorderLayout.LINE_START);

        JPanel right = new JPanel(new BorderLayout());
        right.setPreferredSize(new Dimension(700, 700));
        right.setBorder(blackline);
        mainPanel.add(right, BorderLayout.LINE_END);

        // Welcome panel and label for right panel
        JPanel welcomePanel = makeWelcomePanel();
        right.add(welcomePanel);


        // categories
        JPanel categoriesPanel = new JPanel();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Categories");

        HashMap<String, ArrayList<String>> categoriesAndSubcategories = FileActions.readFile().get(0);
        HashMap<String, ArrayList<Product>> subcategoriesAndProducts = FileActions.readFile().get(1);
        for (Object key : categoriesAndSubcategories.keySet()) {
            DefaultMutableTreeNode category = new DefaultMutableTreeNode(key.toString());

            ArrayList<String> subcategories = categoriesAndSubcategories.get(key.toString());
            for (Object sub : subcategories) {
                DefaultMutableTreeNode subcategory = new DefaultMutableTreeNode(sub.toString());
                category.add(subcategory);
            }

            root.add(category);
        }

        JTree jt = new JTree(root);
        jt.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode =
                        (DefaultMutableTreeNode) jt.getLastSelectedPathComponent();

                if (subcategoriesAndProducts.containsKey(selectedNode.toString())) {
                    ArrayList<Product> products = subcategoriesAndProducts.get(selectedNode.toString());
                    JScrollPane productsCatalog = null;
                    try {
                        Product[] products1= products.toArray(new Product[0]);
                        Arrays.sort(products1);
                        ArrayList<Product> sortedByPriceProducts = new ArrayList<>(List.of(products1));
                        productsCatalog = makeProductsCatalog(sortedByPriceProducts, right);
                        productsCatalog.setVisible(true);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    right.removeAll();
                    right.add(productsCatalog);
                    right.repaint();
                    pack();
                }
            }
        });

        categoriesPanel.add(jt);
        left.add(categoriesPanel);


        this.add(mainPanel);
        this.setVisible(true);
    }
}
