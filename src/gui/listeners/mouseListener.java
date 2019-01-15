package gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import gui.dialogs.EnlargeTable;
import gui.mainEngine.Gui;
import gui.tableElements.commons.ExtendedJvTable;
import gui.tableElements.tableRenderers.IDUTableRenderer;

public class mouseListener {
	Gui gui;
	public mouseListener(Gui gui) {
		this.gui=gui;
	}

	public void listenZoomInButton(Gui gui) {
		gui.getZoomInButton().setBounds(1000, 560, 100, 30);
		gui.getZoomInButton().addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
                gui.setRowHeight(gui.getRowHeight() + 2);
                gui.setColumnWidth(gui.getColumnWidth() + 1);
                gui.getZoomAreaTable().setZoom(gui.getRowHeight(), gui.getColumnWidth());

			}
		});	
		gui.getZoomInButton().setVisible(false);
	}
	
	public void listenZoomOutButton(Gui gui) {
		gui.getZoomOutButton().setBounds(1110, 560, 100, 30);
        gui.getZoomOutButton().addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
                gui.setRowHeight(gui.getRowHeight() - 2);
                gui.setColumnWidth(gui.getColumnWidth() - 1);
                if (gui.getRowHeight() < 1) {
                    gui.setRowHeight(1);
				}
                if (gui.getColumnWidth() < 1) {
                    gui.setColumnWidth(1);
				}
                gui.getZoomAreaTable().setZoom(gui.getRowHeight(), gui.getColumnWidth());

			}
		});
        gui.getZoomOutButton().setVisible(false);
	}
	
	public void listenToEnlarge(Gui gui) {
		gui.getShowThisToPopup().setBounds(800, 560, 100, 30);

        gui.getShowThisToPopup().addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {

                EnlargeTable showEnlargmentPopup = new EnlargeTable(gui.getFinalRowsZoomArea(), gui.getFinalColumnsZoomArea(), gui.getSegmentSizeZoomArea());
				showEnlargmentPopup.setBounds(100, 100, 1300, 700);
				showEnlargmentPopup.setVisible(true);


            }
		});
        
        gui.getShowThisToPopup().setVisible(false);
	}
	
	public void listenUndoButton(Gui gui) {
		 gui.getUndoButton().setBounds(680, 560, 100, 30);

	        gui.getUndoButton().addMouseListener(new MouseAdapter() {
				@Override
				   public void mouseClicked(MouseEvent e) {
	                if (gui.getFirstLevelUndoColumnsZoomArea() != null) {
	                    gui.setFinalColumnsZoomArea(gui.getFirstLevelUndoColumnsZoomArea());
	                    gui.setFinalRowsZoomArea(gui.getFirstLevelUndoRowsZoomArea());
	                    gui.makeZoomAreaTableForCluster();
					}

				}
			});

	        gui.getUndoButton().setVisible(false);

	}
	
	public void listenWidthButton(Gui gui) {
		gui.getUniformlyDistributedButton().setBounds(980, 0, 120, 30);

        gui.getUniformlyDistributedButton().addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
                gui.getLifeTimeTable().uniformlyDistributed(1);

            }
		});

        gui.getUniformlyDistributedButton().setVisible(false);
	}
	
	public void listenOvertimeButton(Gui gui) {
		gui.getNotUniformlyDistributedButton().setBounds(1100, 0, 120, 30);

        gui.getNotUniformlyDistributedButton().addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
                gui.getLifeTimeTable().notUniformlyDistributed(gui.getGlobalDataKeeper());

            }
		});

        gui.getNotUniformlyDistributedButton().setVisible(false);
	}
	
	public void listenToRightClick(Gui gui, ExtendedJvTable generalTable , IDUTableRenderer renderer) {
		generalTable.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 1) {
					JTable target = (JTable)e.getSource();

                    gui.setSelectedRowsFromMouse(target.getSelectedRows());
                    gui.setSelectedColumnZoomArea(target.getSelectedColumn());
                    renderer.setSelCol(gui.getSelectedColumnZoomArea());
			        target.getSelectedColumns();

                    gui.getZoomAreaTable().repaint();
				}

			  }
		});
		
		generalTable.addMouseListener(new MouseAdapter() {
		   public void mouseReleased(MouseEvent e) {

				if(SwingUtilities.isRightMouseButton(e)){
					System.out.println("Right Click");

					JTable target1 = (JTable)e.getSource();
					target1.getSelectedColumns();
					gui.setSelectedRowsFromMouse(target1.getSelectedRows());
					System.out.println(target1.getSelectedColumns().length);
					System.out.println(target1.getSelectedRow());
                 for (int rowsSelected = 0; rowsSelected < gui.getSelectedRowsFromMouse().length; rowsSelected++) {
                     System.out.println(generalTable.getValueAt(gui.getSelectedRowsFromMouse()[rowsSelected], 0));
					}
					final JPopupMenu popupMenu = new JPopupMenu();
			        JMenuItem showDetailsItem = new JMenuItem("Clear Selection");
			        showDetailsItem.addActionListener(new ActionListener() {

			            @Override
			            public void actionPerformed(ActionEvent e) {
                         gui.setSelectedFromTree(new ArrayList<String>());
                         gui.getZoomAreaTable().repaint();
			            }
			        });
			        popupMenu.add(showDetailsItem);
			        popupMenu.show(generalTable, e.getX(),e.getY());


             }

		   }
		});
		
        generalTable.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
                gui.setWholeColZoomArea(generalTable.columnAtPoint(e.getPoint()));
		        renderer.setWholeCol(generalTable.columnAtPoint(e.getPoint()));
		        generalTable.repaint();
		    }
		});

		generalTable.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseReleased(MouseEvent e) {
		    	if(SwingUtilities.isRightMouseButton(e)){
					System.out.println("Right Click");

							final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem showDetailsItem = new JMenuItem("Clear Column Selection");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
                                    gui.setWholeColZoomArea(-1);
                                    renderer.setWholeCol(gui.getWholeColZoomArea());

					            	generalTable.repaint();
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(generalTable, e.getX(),e.getY());

				}

		   }

		});

        gui.setZoomAreaTable(generalTable);
	}
	
	public void listenToRightClick(Gui gui, ExtendedJvTable table) {
		showListener showlistener = new showListener(gui);
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 1) {
					JTable target = (JTable)e.getSource();
	                gui.setSelectedRowsFromMouse(target.getSelectedRows());
	                gui.setSelectedColumn(target.getSelectedColumn());
	                gui.getLifeTimeTable().repaint();
				}

			   }
		});

		table.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseReleased(MouseEvent e) {
					if(e.getButton() == MouseEvent.BUTTON3){
						System.out.println("Right Click");

						JTable target1 = (JTable)e.getSource();
	                    gui.setSelectedColumn(target1.getSelectedColumn());
	                    gui.setSelectedRowsFromMouse(new int[target1.getSelectedRows().length]);
	                    gui.setSelectedRowsFromMouse(target1.getSelectedRows());

						final String sSelectedRow = (String) table.getValueAt(target1.getSelectedRow(),0);
	                    gui.setTablesSelected(new ArrayList<String>());

	                    for (int rowsSelected = 0; rowsSelected < gui.getSelectedRowsFromMouse().length; rowsSelected++) {
	                        gui.getTablesSelected().add((String) table.getValueAt(gui.getSelectedRowsFromMouse()[rowsSelected], 0));
						}

						JPopupMenu popupMenu = new JPopupMenu();
				        JMenuItem showDetailsItem = new JMenuItem("Show Details for the selection");
				        showDetailsItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent le) {
				            	if(sSelectedRow.contains("Cluster ")){
	                                gui.showClusterSelectionToZoomArea(gui.getSelectedColumn(), sSelectedRow);

				            	}
				            	else{
	                                gui.showSelectionToZoomArea(gui.getSelectedColumn());
				            	}
				            }
				        });
				        popupMenu.add(showDetailsItem);
				        JMenuItem clearSelectionItem = new JMenuItem("Clear Selection");
				        clearSelectionItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent le) {

	                            gui.setSelectedFromTree(new ArrayList<String>());
	                            gui.getLifeTimeTable().repaint();
				            }
				        });
				        popupMenu.add(clearSelectionItem);
				        popupMenu.show(table, e.getX(),e.getY());

					}

			   }
		});

		table.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
	            gui.setWholeCol(table.columnAtPoint(e.getPoint()));
	            String name = table.getColumnName(gui.getWholeCol());
	            System.out.println("Column index selected " + gui.getWholeCol() + " " + name);
		        table.repaint();
	            if (gui.isShowingPld()) {
	                gui.makeGeneralTableIDU();
				}
		    }
		});

		table.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseReleased(MouseEvent e) {
		    	if(SwingUtilities.isRightMouseButton(e)){
					System.out.println("Right Click");

							final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem clearColumnSelectionItem = new JMenuItem("Clear Column Selection");
					        clearColumnSelectionItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
	                                gui.setWholeCol(-1);
					            	table.repaint();
	                                if (gui.isShowingPld()) {
	                                    gui.makeGeneralTableIDU();
					            	}
					            }
					        });
					        popupMenu.add(clearColumnSelectionItem);
					        JMenuItem showDetailsItem = new JMenuItem("Show Details for this Phase");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
					            	
									showlistener.showDetailsForPhase(table);
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(table, e.getX(),e.getY());

				}

		   }

		});

	    gui.setLifeTimeTable(table);
	}
	
	
	public void listenToRightClick2(ExtendedJvTable zoomTable) {
		zoomTable.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 1) {
					JTable target = (JTable)e.getSource();

	                gui.setSelectedRowsFromMouse(target.getSelectedRows());
	                gui.setSelectedColumnZoomArea(target.getSelectedColumn());
	                gui.getZoomAreaTable().repaint();
				}

			   }
		});

		zoomTable.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseReleased(MouseEvent e) {

					if(SwingUtilities.isRightMouseButton(e)){
						System.out.println("Right Click");

						JTable target1 = (JTable)e.getSource();
	                    gui.setSelectedColumnZoomArea(target1.getSelectedColumn());
	                    gui.setSelectedRowsFromMouse(target1.getSelectedRows());
						System.out.println(target1.getSelectedColumn());
						System.out.println(target1.getSelectedRow());
						final ArrayList<String> tablesSelected = new ArrayList<String>();
	                    for (int rowsSelected = 0; rowsSelected < gui.getSelectedRowsFromMouse().length; rowsSelected++) {
	                        tablesSelected.add((String) zoomTable.getValueAt(gui.getSelectedRowsFromMouse()[rowsSelected], 0));
							System.out.println(tablesSelected.get(rowsSelected));
						}


	                }

			   }
		});

		// listener
		zoomTable.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
	            gui.setWholeColZoomArea(zoomTable.columnAtPoint(e.getPoint()));
	            String name = zoomTable.getColumnName(gui.getWholeColZoomArea());
	            System.out.println("Column index selected " + gui.getWholeCol() + " " + name);
		        zoomTable.repaint();
		    }
		});

		zoomTable.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseReleased(MouseEvent e) {
		    	if(SwingUtilities.isRightMouseButton(e)){
					System.out.println("Right Click");

							final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem showDetailsItem = new JMenuItem("Clear Column Selection");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
	                                gui.setWholeColZoomArea(-1);
					            	zoomTable.repaint();
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(zoomTable, e.getX(),e.getY());

				}

		   }

		});


	    gui.setZoomAreaTable(zoomTable);
	}
	
	public void listenToRightClick3(ExtendedJvTable zoomTable) {
	    zoomTable.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {

	            if (e.getClickCount() == 1) {
					JTable target = (JTable)e.getSource();

	                gui.setSelectedRowsFromMouse(target.getSelectedRows());
	                gui.setSelectedColumnZoomArea(target.getSelectedColumn());
	                gui.getZoomAreaTable().repaint();
				}

	        }
		});

	    zoomTable.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseReleased(MouseEvent e) {

	            if(SwingUtilities.isRightMouseButton(e)){
						System.out.println("Right Click");

							JTable target1 = (JTable)e.getSource();
	                gui.setSelectedColumnZoomArea(target1.getSelectedColumn());
	                gui.setSelectedRowsFromMouse(target1.getSelectedRows());
							System.out.println(target1.getSelectedColumn());
							System.out.println(target1.getSelectedRow());

	                gui.setTablesSelected(new ArrayList<String>());

	                for (int rowsSelected = 0; rowsSelected < gui.getSelectedRowsFromMouse().length; rowsSelected++) {
	                    gui.getTablesSelected().add((String) zoomTable.getValueAt(gui.getSelectedRowsFromMouse()[rowsSelected], 0));
	                    System.out.println(gui.getTablesSelected().get(rowsSelected));
							}
	                if (zoomTable.getColumnName(gui.getSelectedColumnZoomArea()).contains("Phase")) {

								final JPopupMenu popupMenu = new JPopupMenu();
						        JMenuItem showDetailsItem = new JMenuItem("Show Details");
						        showDetailsItem.addActionListener(new ActionListener() {

						            @Override
						            public void actionPerformed(ActionEvent e) {
	                                    gui.setFirstLevelUndoColumnsZoomArea(gui.getFinalColumnsZoomArea());
	                                    gui.setFirstLevelUndoRowsZoomArea(gui.getFinalRowsZoomArea());
	                                    gui.showSelectionToZoomArea(gui.getSelectedColumnZoomArea());


						            }
						        });
						        popupMenu.add(showDetailsItem);
						        popupMenu.show(zoomTable, e.getX(),e.getY());
			            	}


	            }

	        }
		});

	    // listener
		zoomTable.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
	            gui.setWholeColZoomArea(zoomTable.columnAtPoint(e.getPoint()));
	            String name = zoomTable.getColumnName(gui.getWholeColZoomArea());
	            System.out.println("Column index selected " + gui.getWholeCol() + " " + name);
		        zoomTable.repaint();
		    }
		});

	    zoomTable.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseReleased(MouseEvent e) {
		    	if(SwingUtilities.isRightMouseButton(e)){
					System.out.println("Right Click");

	                final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem showDetailsItem = new JMenuItem("Clear Column Selection");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
	                                gui.setWholeColZoomArea(-1);
					            	zoomTable.repaint();
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(zoomTable, e.getX(),e.getY());

	            }

	        }

	    });


	    gui.setZoomAreaTable(zoomTable);
	}
	

	
	public void listenTreeSelection(JTree tree) {
		tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent ae) {
			    	TreePath selection = ae.getPath();
                gui.getSelectedFromTree().add(selection.getLastPathComponent().toString());
                System.out.println(selection.getLastPathComponent() + " is selected");
			    }
		 });

        tree.addMouseListener(new MouseAdapter() {
				@Override
				   public void mouseReleased(MouseEvent e) {

                    if(SwingUtilities.isRightMouseButton(e)){
							System.out.println("Right Click Tree");

                        final JPopupMenu popupMenu = new JPopupMenu();
							        JMenuItem showDetailsItem = new JMenuItem("Show This into the Table");
							        showDetailsItem.addActionListener(new ActionListener() {

                                        @Override
							            public void actionPerformed(ActionEvent e) {

                                            gui.getLifeTimeTable().repaint();

							            }
							        });
							        popupMenu.add(showDetailsItem);
                        popupMenu.show(tree, e.getX(), e.getY());

						}

                }
			});
	}
}