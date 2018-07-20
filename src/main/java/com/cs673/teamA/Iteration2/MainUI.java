package com.cs673.teamA.Iteration2;

import javax.servlet.annotation.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Image;
import java.util.*;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.Window;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.BrowserFrame;


@Theme("mytheme")
@SuppressWarnings("serial")
@SpringUI
public class MainUI extends UI {
	//Project filter, which is in the side menu.
    private  TextField projectFilter;
    private  Button projectFilterBtn;

    //Fixed size main operation panel, reloading it when needed.
    private Panel mainPanel;
    
    //Repositories and other DB objects
  	@Autowired
  	private IssueRepository iRepo;
  	
  	@Autowired
  	private CommentRepository cRepo;
  	
  	@Autowired
  	private ProjectRepository pRepo;
  	
  	@Autowired
  	private UserProfileRepository uRepo;

    //!!!!!!!!!!!!!!!!!!!!THIS IS NEW!!!!!!!!!!!!!!!!!
    @Autowired
    private UpwRepository upwRepository;
  	
  	private List<Long> issues;
  	private List<Long> projects;
  	private Long selectedProject;
    private Long selectedIssue;
  	private List<Comment> comments;
    //!!!!!!!!!!!!MORE NEW!!!!!!!!!!!
    private String authToken;
    private UserProfile loggedIn;
    //END NEW
    
    //Define max display of dynamically added components.
    private final int MAX_PROJECTS_NUM = 5;
    private final int MAX_ISSUES_NUM = 25;
    private final int MAX_COMMENTS_NUM = 100;
    private final int MAX_STRING_LENGTH = 255;
    
    //Other layout that may be changed real time.
    private CustomLayout mainPanelLayout;

    //This is the rootRootLayout for adding pop up window.
    private AbsoluteLayout rootRootLayout;
    private PopupView popIssueView;
    private PopupView popCommentView;
    private PopupView popLoginView;

    //Pop up window UI for CRUD on issue tickets.
    //Easier to control UI components when they are global variables in this MainUI class.
    private VerticalLayout popupContent;
    private TextField issueTitleText;
    private TextArea issueContentText;
    private TextField assigneeText;
    private TextField ownerText;
    private Button addIssueButton;

    //Pop up view for login.
    VerticalLayout popLoginLayout;
    TextField popLoginUser;
    TextField popLoginPwd;
    Button popLoginBtn;

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MainUI.class)
    public static class Servlet extends VaadinServlet {
    }

    //Login functionality for authentication
    public String login(String un, String pw) {
        Upw u = upwRepository.findByUnAndPw(un,pw);
        if (u != null) {
            return SecurityTest.encrypt(u.getId());
        }
        else {
            return Integer.toString(-1);
        }
    }

    private void loadIssueComments(Long issueId) {
    	IssueTicket issue = iRepo.findById(issueId).get();
        if (!comments.isEmpty()) {
            comments.clear();
        }
        comments.addAll(cRepo.findByIssueId(issueId));
        CustomLayout commentsBoardContent = new CustomLayout("discussion_board");
        Label issueName = new Label(issue.getName());
        issueName.setWidth(900, Unit.PIXELS);
        issueName.addStyleName("myCommentTitle");
        Label issueInfo = new Label(issue.getDescription());
        issueInfo.setWidth(900, Unit.PIXELS);
        issueInfo.addStyleName("myCommentStatus");

        Button backButton = new Button("Back");
        backButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                loadIssueTickets(issue.getProjectId()); //Go back and reload the whole issue ticket.
            }
        });
        
        VerticalLayout commentsRoot = new VerticalLayout();
        //Adding comments to the comments board.
        for (int i=0; i<MAX_COMMENTS_NUM; i++) {
        	if (i == comments.size()) {
        		break;
        	}
            // Get the profile icon from resource.
            String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
            FileResource resource = new FileResource(new File(basepath + "/WEB-INF/images/mySelfie.jpg"));
            Image mySelfie = new Image("", resource);
            mySelfie.setHeight(80, Unit.PIXELS);

            CustomLayout commentSection = new CustomLayout("comment");
            commentSection.addComponent(mySelfie, "profileIcon");
            Label comment = new Label(comments.get(i).getContent());
            comment.setWidth(700, Unit.PIXELS);
            commentSection.addComponent(comment, "comment");
            
            commentsRoot.addComponent(commentSection);
    	}
        //Add the add comment button.
        Button addComment = new Button("Add Comment");
        addComment.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                popCommentView.setPopupVisible(true);
            }
        });
        //Add a refresh button for "close to" real time user experience.
        Button refreshComment = new Button("Refresh");
        refreshComment.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                //reload comment page.
                loadIssueComments(selectedIssue);
            }
        });
        commentsRoot.addComponent(addComment);
        commentsRoot.setComponentAlignment(addComment, Alignment.MIDDLE_CENTER);
        //Add the refresh button to the right.
        commentsRoot.addComponent(refreshComment);
        commentsRoot.setComponentAlignment(refreshComment, Alignment.MIDDLE_CENTER);
        commentsBoardContent.addComponent(commentsRoot, "commentsRoot");
        commentsBoardContent.addComponent(issueName, "title");
        commentsBoardContent.addComponent(issueInfo, "info");
        commentsBoardContent.addComponent(backButton, "backButton");
        this.mainPanel.setContent(commentsBoardContent);
    }
    
    private void loadIssueTickets(Long projectId) {
        //Issue Tickets filter
        TextField issueFilter;
        Button issueFilterBtn;

        CustomLayout mainPanelContent = new CustomLayout("panel_content");
        //Issue Tickets filter
        issueFilter = new TextField();
        issueFilter.setPlaceholder("Issue Title");
        issueFilterBtn = new Button("Search");
        mainPanelContent.setHeightUndefined();
        HorizontalLayout issueFilterLayout = new HorizontalLayout();
        issueFilterLayout.addComponent(issueFilterBtn);
        issueFilterLayout.setComponentAlignment(issueFilterBtn, Alignment.MIDDLE_LEFT);
        issueFilterLayout.addComponent(issueFilter);
        issueFilterLayout.setComponentAlignment(issueFilter, Alignment.MIDDLE_LEFT);
        mainPanelContent.addComponent(issueFilterLayout, "issueFilter");
        HorizontalLayout issueButtonsLayout = new HorizontalLayout();
        Button newIssueBtn = new Button("New issue");
        newIssueBtn.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                //Make empty pop up view for creating new issue ticket.
                makeIssuePopupView("", "", "", "");
                //Calling issue popup view, but not querying the database.
                selectedIssue = Long.MIN_VALUE;
                popIssueView.setPopupVisible(true);
            }
        });
        issueButtonsLayout.addComponent(newIssueBtn);
        issueButtonsLayout.setComponentAlignment(newIssueBtn, Alignment.MIDDLE_RIGHT);
        mainPanelContent.addComponent(issueButtonsLayout, "issueButtons");

        //Dynamically add the tickets to the panel.
        VerticalLayout issueTicketsBoard = new VerticalLayout();
        if (!issues.isEmpty()) {
        	issues.clear();
        }
    	iRepo.findByProjectId(projectId).forEach(issue -> issues.add(issue.getIssueId()));
        
        for (int i=0; i<MAX_ISSUES_NUM; i++) {
        	
            if (i == issues.size()) {
                break;
            }
            IssueTicket issue = iRepo.findById(issues.get(i)).get();
            // Get the opened icon from resource.
            String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
            FileResource openRsrc = new FileResource(new File(basepath + "/WEB-INF/images/opened.png"));
            FileResource closedRsrc = new FileResource(new File(basepath + "/WEB-INF/images/closed.png"));
            //Check if this issue ticket is resolved.
            Button resolvedBtn = new Button();
            resolvedBtn.setHeight(25, Unit.PIXELS);
            resolvedBtn.setWidth(25, Unit.PIXELS);
            if (issue.isResolved()) {
                //Use closed icon.
                resolvedBtn.setIcon(closedRsrc);
                resolvedBtn.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
                resolvedBtn.addClickListener(new Button.ClickListener() {
                    public void buttonClick(ClickEvent event) {
                        //Change iRepo data of this issue's isResolved(), and reload issue tickets.
                        issue.setResolved(false);
                        iRepo.save(issue);
                        loadIssueTickets(selectedProject);
                    }
                });
            } else {
                //Use open icon.
                resolvedBtn.setIcon(openRsrc);
                resolvedBtn.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
                resolvedBtn.addClickListener(new Button.ClickListener() {
                    public void buttonClick(ClickEvent event) {
                        //Change iRepo data of this issue's isResolved(), and reload issue tickets.
                        issue.setResolved(true);
                        iRepo.save(issue);
                        loadIssueTickets(selectedProject);
                    }
                });
            }

            //D-D: FileResource resource = new FileResource(new File(basepath + "/WEB-INF/images/opened.png"));
            //Image openedIcon = new Image("", resource);
            //openedIcon.setHeight(25, Unit.PIXELS);

            // Get the comment icon from resource.
            //D-D: basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
            FileResource resource = new FileResource(new File(basepath + "/WEB-INF/images/comment.png"));
            Image commentIcon = new Image("", resource);
            commentIcon.setHeight(20, Unit.PIXELS);
            Label commentNum = new Label("9");

            String tempIssueName = issue.getName();
            // Create customized issue tickets.
            CustomLayout ticket = new CustomLayout("issue_ticket");
            Button issueTitle = new Button(tempIssueName);
            issueTitle.setIconAlternateText(tempIssueName);
            issueTitle.addStyleName("titleOnClick");
            issueTitle.addClickListener(new Button.ClickListener() {
                public void buttonClick(ClickEvent event) {
                    //Use the issue ID to query the database.
                    selectedIssue = issue.getIssueId();
                    //ToDo: I still think this code is amazing.
                    loadIssueComments(issue.getIssueId());
                }
            });

            //Layout for issue status, owner and assignee.
            HorizontalLayout issueInfo = new HorizontalLayout();
            //Label issueStatus = new Label(issue.getDescription());
            Label issueOwner = new Label("Owner: " + uRepo.findById(issue.getOwnerId()).get().getUsername());
            Label issueAssignee = new Label("Assignee: " + uRepo.findById(issue.getAssigneeId()).get().getUsername());
            //issueInfo.addComponent(issueStatus);
            issueInfo.addComponent(issueOwner);
            issueInfo.addComponent(issueAssignee);
            //issueStatus.addStyleName("myIssueStatus");
            //Layout for edit issue button and comment icon.
            HorizontalLayout issueTicketRight = new HorizontalLayout();
            Button editIssueButton = new Button("Edit");
            editIssueButton.addClickListener(new Button.ClickListener() {
                public void buttonClick(ClickEvent event) {
                    //Pop up a window for editing issue information, show issue data in blanks.
                    //ToDo: assignee and owner is not connected to login yet.
                    makeIssuePopupView(issue.getName(), issue.getDescription(), "", "");
                    //Use the issue ID to query the database.
                    selectedIssue = issue.getIssueId();
                    popIssueView.setPopupVisible(true);
                }
            });
            //Delete issue by issue id.
            Button deleteIssueButton = new Button("Delete");
            deleteIssueButton.addClickListener(new Button.ClickListener() {
                public void buttonClick(ClickEvent event) {
                    iRepo.deleteById(issue.getIssueId());
                    loadIssueTickets(selectedProject);
                }
            });

            issueTicketRight.addComponent(editIssueButton);
            issueTicketRight.addComponent(deleteIssueButton);
            issueTicketRight.addComponent(commentIcon);
            //Add components to the customized ticket layout.
            ticket.addComponent(resolvedBtn, "resolved");
            ticket.addComponent(issueTitle, "issueTitle");
            ticket.addComponent(issueInfo, "issueStatus");
            ticket.addComponent(issueTicketRight, "commentIcon");
            ticket.addComponent(commentNum, "commentNum");
            
            issueTicketsBoard.addComponent(ticket);
        }
        mainPanelContent.addComponent(issueTicketsBoard, "issueTicketsBoard");

        //Set the panel content to the fixed size panel.
        this.mainPanel.setContent(mainPanelContent);
    }

    private void makeIssuePopupView(String title, String content, String assignee, String owner) {
        issueTitleText.setValue(title);
        issueContentText.setValue(content);
        assigneeText.setValue(assignee);
        ownerText.setValue(owner);
    }

    private void makeHeaderLayout(boolean isLogin, String userName) {
        HorizontalLayout headerLayout = new HorizontalLayout();
        Button pmBtn = new Button("Project Management Tool");
        pmBtn.setHeight(65, Unit.PIXELS);
        pmBtn.addStyleName("headerButton");

        Button chatBtn = new Button("Communication Tool");
        chatBtn.setHeight(65, Unit.PIXELS);
        chatBtn.addStyleName("headerButton");
        chatBtn.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                // Create a sub-window and set the content
                Window subWindow = new Window("Communication Tool");
                BrowserFrame browser = new BrowserFrame("Browser",
                new ExternalResource("https://friendlychat-1f1b0.firebaseapp.com/"));
                browser.setWidth("300px");
                browser.setHeight("500px");
                VerticalLayout subWindowLayout = new VerticalLayout();
                subWindowLayout.addComponent(browser);

                subWindow.setContent(subWindowLayout);

                // Center it in the browser window
                subWindow.center();

                // Open it in the UI
                addWindow(subWindow);
            }
        });
        
        // Need to change this label after user is login.
        Label loginInfo = new Label("Hello " + userName);

        Button loginBtn = new Button("Login");
        //issueTitle.setIconAlternateText(tempIssueName);
        loginBtn.addStyleName("titleOnClick");
        loginBtn.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                //Show the login popup view.
                popLoginView.setPopupVisible(true);
            }
        });

        headerLayout.addComponent(pmBtn);
        headerLayout.setComponentAlignment(pmBtn, Alignment.MIDDLE_RIGHT);
        headerLayout.addComponent(chatBtn);
        headerLayout.setComponentAlignment(chatBtn, Alignment.MIDDLE_RIGHT);

        if (isLogin == true) {
            //TODO: Need to do log out button as well.
            headerLayout.addComponent(loginInfo);
            headerLayout.setComponentAlignment(loginInfo, Alignment.MIDDLE_RIGHT);
        } else {
            //This is changed after user login.
            headerLayout.addComponent(loginBtn);
            headerLayout.setComponentAlignment(loginBtn, Alignment.MIDDLE_RIGHT);
        }
        
        //reload the header layout in the mainPanelLayout.
        mainPanelLayout.addComponent(headerLayout, "header");
    }
        
    @Override
    protected void init(VaadinRequest request) {
        if (!pRepo.findAll().iterator().hasNext()) {
            //Not keep creating dummy projects
            Project projA = new Project();
            projA.setProjectName("CS673 Class Project");
            pRepo.save(projA);
            Project projB = new Project();
            projB.setProjectName("Project B");
            pRepo.save(projB);
            Project projC = new Project();
            projC.setProjectName("Project C");
            pRepo.save(projC);
            Project projX = new Project();
            projX.setProjectName("Project X");
            pRepo.save(projX);
            Project projY = new Project();
            projY.setProjectName("Project Y");
            pRepo.save(projY);
            Project projZ = new Project();
            projZ.setProjectName("Project Z");
            pRepo.save(projZ);
        }
    	
    	projects = new ArrayList<Long>();
    	pRepo.findAll().forEach(project -> projects.add(project.getProjectId()));
    	
        //Demo users
        if (uRepo.findByUsername("Guest") == null) {
            UserProfile guest = new UserProfile();
            guest.setUsername("Guest");
            uRepo.save(guest);
        }
        if (upwRepository.findByUnContaining("Alex Andrade").isEmpty()) {
            Upw alexUpw = new Upw();
            alexUpw.setUn("Alex Andrade");
            alexUpw.setPw("AlexDemo!");
            upwRepository.save(alexUpw);
            UserProfile alex = new UserProfile();
            alex.setUsername("Alex Andrade");
            uRepo.save(alex);
        }
        if (upwRepository.findByUnContaining("I-Yang Chen").isEmpty()) {
            Upw iYangUpw = new Upw();
            iYangUpw.setUn("I-Yang Chen");
            iYangUpw.setPw("I-YangDemo!");
            upwRepository.save(iYangUpw);
            UserProfile iYang = new UserProfile();
            iYang.setUsername("I-Yang Chen");
            uRepo.save(iYang);
        }
        
        //THIS DOESN'T WORK CORRECTLY YET, AND WILL LOG IN AS GUEST
        //log in Alex automatically
        authToken = login("Alex Andrade", "AlexDemo!");
        int decrypted = SecurityTest.decrypt(authToken);
        if (decrypted != -1) {
            loggedIn = uRepo.findByUsername(upwRepository.findById(decrypted).get().getUn());
        }
        //If authentication fails, guest.
        else {
            loggedIn = uRepo.findByUsername("Guest");
        }
    	
    	issues = new ArrayList<Long>();
        //Do the same lambda here, so issues will contain all the IDs.
        iRepo.findAll().forEach(issue -> issues.add(issue.getIssueId()));

    	comments = new ArrayList<Comment>();
    	
        /**
         * UI - Side Menu
         */

        // Project filter
        this.projectFilter = new TextField();
        this.projectFilter.setPlaceholder("Project Name");
        this.projectFilterBtn = new Button("Search");
        HorizontalLayout sideMenuSearchDiv = new HorizontalLayout(this.projectFilter, this.projectFilterBtn);

        Label sideMenuTitle = new Label("Projects");
        sideMenuTitle.addStyleName("side_menu_title");
        Label sideMenuPageNavi = new Label("Page Navigator");
        sideMenuPageNavi.addStyleName("pageNavigatorTemp");

        CustomLayout sideMenuLayout = new CustomLayout("side_menu");
        sideMenuLayout.addComponent(sideMenuTitle, "Projects");
        sideMenuLayout.addComponent(sideMenuSearchDiv, "searchProjects");
        sideMenuLayout.addComponent(sideMenuPageNavi, "PageNavigator");

        /*
         * TODOs:
         * (1) Loading project names from the database, need an algorithm to deal with more than
         * five projects.
         * (2) Add button listener to reload the main panel content, which is the issue tickets.
         */

        List<Button> projectButtons = new ArrayList<>();
        for (int i=0; i<projects.size(); i++) {
        	if (i == projects.size()) {
        		break;
        	}
        	Project project = pRepo.findById(projects.get(i)).get();
            Button temp = new Button(project.getProjectName());
            temp.addStyleName("sideMenuButton");
            temp.setWidth(300, Unit.PIXELS);
            temp.setHeight(80, Unit.PIXELS);
            temp.addClickListener(new Button.ClickListener() {
            	public void buttonClick(ClickEvent event) {
            		selectedProject = project.getProjectId();
            		loadIssueTickets(selectedProject);
            	}
            });
            projectButtons.add(temp);
        }
        for (int i=0; i<MAX_PROJECTS_NUM; i++) {
            String location = "Project_Btn";
            sideMenuLayout.addComponent(projectButtons.get(i), location + Integer.toString(i+1));
        }

        /**
         * UI - Main Page 
         */
        mainPanelLayout = new CustomLayout("main_panel_layout");

        /**
         * UI - Header 
         */
        makeHeaderLayout(false, null);
        
        //Define the fixed size operation panel here. 
        this.mainPanel = new Panel();
        mainPanel.setWidth(1000, Unit.PIXELS);
        mainPanel.setHeight(500, Unit.PIXELS);
        mainPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);

        selectedProject = projects.get(0);
        this.loadIssueTickets(selectedProject);

        //Add the fixed size panel to the main panel layout.
        mainPanelLayout.addComponent(mainPanel, "list_view");

        /**
         * UI - Footer
         */
        Label footerLayout = new Label("CS673 - Team A, Issue Management Tool, UI Created by I-Yang Chen");
        footerLayout.addStyleName("footerLayout");
        mainPanelLayout.addComponent(footerLayout, "footerLayout");
        
        //Root, add everything together and display. 
        //Try using absolute layout as the root layout.
        AbsoluteLayout rootLayout = new AbsoluteLayout();
        sideMenuLayout.setSizeFull(); // Fill the specified area
        mainPanelLayout.setSizeFull();
        rootLayout.addComponent(sideMenuLayout, "left: 0%; right: 80%;" +
                           "top: 0%; bottom: 0%;");
        rootLayout.addComponent(mainPanelLayout, "left: 300px; right: 5%;" +
                           "top: 5%; bottom: 0%;");

        // Content for the PopupView of creating new issue.
        popupContent = new VerticalLayout();
        popupContent.setSizeFull();
        issueTitleText = new TextField("Issue Title");
        issueTitleText.setWidth(300, Unit.PIXELS);
        issueContentText = new TextArea("Issue Content");
        issueContentText.setHeight(200, Unit.PIXELS);
        issueContentText.setWidth(300, Unit.PIXELS);
        assigneeText = new TextField("Assignee");
        assigneeText.setWidth(300, Unit.PIXELS);
        ownerText = new TextField("Owner");
        ownerText.setWidth(300, Unit.PIXELS);
        addIssueButton = new Button("Ok");
        addIssueButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                //Check that the issue's title meets requirements.
                if (issueTitleText.isEmpty()) {
                    Notification.show("Issue must have a title.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                else if (issueTitleText.getValue().length() > MAX_STRING_LENGTH) {
                    Notification.show("Your issue title must contain 255 characters or fewer.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                
                //Check that the issue's description meets length requirement.
                //Description can be empty.
                if (issueContentText.getValue().length() > MAX_STRING_LENGTH) {
                    Notification.show("Your issue description must contain 255 characters or fewer.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                
                //Search the database for users containing the entered values for owner and assignee.
                //ToDo: Owner and assignee are empty for now.
                List<UserProfile> ownerSearch;
                List<UserProfile> assigneeSearch;
                Long ownerId = null;
                Long assigneeId = null;
                //ToDo: need to get this wired to the database.
                if (!ownerText.isEmpty()) {
                    ownerSearch = uRepo.findByUsernameContainingIgnoreCase(ownerText.getValue());
                    if (ownerSearch.isEmpty()) {
                        Notification.show("No users found with a username containing " 
                            + ownerText.getValue() + ". Please try a different username.", 
                            Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                    else if (ownerSearch.size() > 1) {
                        Notification.show("More than one user was found with a username containing " 
                                + ownerText.getValue() + ". Please try a different username.", 
                                Notification.Type.ERROR_MESSAGE);
                            return;
                    }
                    else {
                        ownerId = ownerSearch.get(0).getUserId();
                    }
                }
                else {
                    ownerId = loggedIn.getUserId();
                }
                if (!assigneeText.isEmpty()) {
                    assigneeSearch = uRepo.findByUsernameContainingIgnoreCase(assigneeText.getValue());
                    if (assigneeSearch.size() == 0) {
                        Notification.show("No users found with a username containing " 
                            + assigneeText.getValue() + ". Please try a different username.", 
                            Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                    else if (assigneeSearch.size() > 1) {
                        Notification.show("More than one user was found with a username containing " 
                                + assigneeText.getValue() + ". Please try a different username.", 
                                Notification.Type.ERROR_MESSAGE);
                            return;
                    }
                    else {
                        assigneeId = assigneeSearch.get(0).getUserId();
                    }
                }
                else {
                    assigneeId = loggedIn.getUserId();
                }
                //Find the project issue counter.
                for (int i=0; i<projects.size(); i++) {

                }
                //Decide create new or edit old according to the value of selectedIssue.
                if (selectedIssue == Long.MIN_VALUE) {
                    //Add a new ticket to the database.
                    IssueTicket newIssue = new IssueTicket();
                    newIssue.setName(issueTitleText.getValue());
                    newIssue.setDescription(issueContentText.getValue());
                    //ToDo: code below doesn't do anything for now.
                    newIssue.setAssigneeId(assigneeId);
                    newIssue.setOwnerId(ownerId);
                    newIssue.setDateCreated(new Date());
                    newIssue.setResolved(false);
                    newIssue.setProjectId(selectedProject);
                    iRepo.save(newIssue);
                    Notification.show(issueTitleText.getValue(), "is created.",
                      Notification.Type.HUMANIZED_MESSAGE);
                    popIssueView.setPopupVisible(false);
                } else {
                    //Edit an old ticket in the database.
                    IssueTicket editIssue = iRepo.findById(selectedIssue).get();
                    editIssue.setName(issueTitleText.getValue());
                    editIssue.setDescription(issueContentText.getValue());
                    iRepo.save(editIssue);
                    Notification.show(issueTitleText.getValue(), "is edited.",
                      Notification.Type.HUMANIZED_MESSAGE);
                    popIssueView.setPopupVisible(false);
                }

                //Reload the issue page.
                loadIssueTickets(selectedProject);
            }
        });
        popupContent.addComponent(issueTitleText);
        popupContent.addComponent(issueContentText);
        popupContent.addComponent(assigneeText);
        popupContent.addComponent(ownerText);
        popupContent.addComponent(addIssueButton);
        popupContent.setComponentAlignment(addIssueButton, Alignment.MIDDLE_CENTER);
        // The component itself
        popIssueView = new PopupView(null, popupContent);
        popIssueView.setHideOnMouseOut(false);

        // Content for the PopupView of creating new comment.
        VerticalLayout popupContent2 = new VerticalLayout();
        popupContent2.setSizeFull();
        TextArea commentContentText = new TextArea("Comment Content");
        commentContentText.setHeight(200, Unit.PIXELS);
        commentContentText.setWidth(300, Unit.PIXELS);
        Button addCommentButton = new Button("Add");
        addCommentButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                //Check that the issue's comment meets requirements.
                if (commentContentText.isEmpty()) {
                    Notification.show("No comment entered.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                else if (commentContentText.getValue().length() > MAX_STRING_LENGTH) {
                    Notification.show("Your comment must contain 255 characters or fewer.", Notification.Type.ERROR_MESSAGE);
                    return;
                }

                //Add the ticket to the database.
                Comment newComment = new Comment();
                newComment.setContent(commentContentText.getValue());
                newComment.setIssueId(selectedIssue);
                cRepo.save(newComment);
                popCommentView.setPopupVisible(false);

                //Reload the comment board.
                loadIssueComments(selectedIssue);
            }
        });
        popupContent2.addComponent(commentContentText);
        popupContent2.addComponent(addCommentButton);
        popupContent2.setComponentAlignment(addCommentButton, Alignment.MIDDLE_CENTER);
        // The component itself
        popCommentView = new PopupView(null, popupContent2);
        popCommentView.setHideOnMouseOut(false);

        // Popup View for Log in.
        VerticalLayout popLoginLayout = new VerticalLayout();
        popLoginLayout.setSizeFull();
        TextField popLoginUser = new TextField("User Name");
        PasswordField popLoginPwd = new PasswordField("Password");
        Button popLoginBtn = new Button("Login");
        popLoginBtn.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                //Check that the login input.
                if (popLoginUser.isEmpty()) {
                    Notification.show("No User Name entered.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                else if (popLoginUser.getValue().length() > MAX_STRING_LENGTH) {
                    Notification.show("User Name must contain 255 characters or fewer.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                //Check that the login input.
                if (popLoginPwd.isEmpty()) {
                    Notification.show("No Password entered.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                else if (popLoginPwd.getValue().length() > MAX_STRING_LENGTH) {
                    Notification.show("Password must contain 255 characters or fewer.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                //ToDo: use the user name and password to login.
                Notification.show("Login not implemented, login as guest.", Notification.Type.ERROR_MESSAGE);
                popLoginView.setPopupVisible(false);
                //login successfully, make new header layout.
                makeHeaderLayout(true, "Guest");
            }
        });
        popLoginLayout.addComponent(popLoginUser);
        popLoginLayout.addComponent(popLoginPwd);
        popLoginLayout.addComponent(popLoginBtn);
        popLoginLayout.setComponentAlignment(popLoginBtn, Alignment.MIDDLE_CENTER);
        // The component itself
        popLoginView = new PopupView(null, popLoginLayout);
        popLoginView.setHideOnMouseOut(false);

        rootRootLayout = new AbsoluteLayout();
        rootRootLayout.addComponent(rootLayout, "left: 0%; right: 0%;" +
                           "top: 0%; bottom: 0%;");
        rootRootLayout.addComponent(popIssueView, "left: 50%; top: 50%;");
        rootRootLayout.addComponent(popCommentView, "left: 50%; top: 50%;");
        rootRootLayout.addComponent(popLoginView, "left: 50%; top: 50%;");
        setContent(rootRootLayout);  
    }
}
