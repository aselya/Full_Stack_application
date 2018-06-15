package issue_layout;

import org.springframework.util.StringUtils;

import com.vaadin.server.FontAwesome;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;

@SpringUI
public class IssueUI extends UI {
	/* Later make a new class for issue tickets to replace customer class. */
	private final TicketRepository repo;

	//private final TicketEditor editor;

	final Grid<Ticket> grid;

	final TextField filter;

	private MenuBar filterMenu;

	private final Button addNewBtn;

	private String filterBy;

	public IssueUI(TicketRepository repo) {
		this.repo = repo;
		this.grid = new Grid<>(Ticket.class);
		this.filter = new TextField();
		this.filter.setPlaceholder("Filter by Project Name"); //Default filter.
		this.filterMenu = new MenuBar();
		this.addNewBtn = new Button("New issue", FontAwesome.PLUS);
		this.filterBy = "Project Name"; //Default is project name.
	}

	@Override
	protected void init(VaadinRequest request) {	
		setTheme("issue_layout");

		CustomLayout layout = new CustomLayout("mainlayout");
		layout.setSizeFull();
		setContent(layout);
		
		Label centerLabel = new Label("Center");
		centerLabel.addStyleName("centerlabel");
		
		Button.ClickListener leftListener = new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				centerLabel.setValue(event.getButton().getCaption());
				
			}
		};

		Button firstButton = new Button("Project 1");
		firstButton.setWidth("100%");
		firstButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		Button secondButton = new Button("Project 2");
		secondButton.setWidth("100%");
		secondButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		Button thirdButton = new Button("Project 3");
		thirdButton.setWidth("100%");
		thirdButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		
		MenuItem filterItem = filterMenu.addItem("Set Filter", null, null);
		// A new filter will be set when a click event on the menu bar take place.
		MenuBar.Command filterCommand = new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				filter.setValue("");
				filter.setPlaceholder("Filter by " + selectedItem.getText());
				filterBy = selectedItem.getText();
			}
		};
		// The grid view is changed accordingly, use filterBy to filter.
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listTickets(e.getValue()));

		MenuItem filterProjectName = filterItem.addItem("Project Name", null, filterCommand);
		MenuItem filterIssueTitle = filterItem.addItem("Issue Title", null, filterCommand);
		MenuItem filterAuthor = filterItem.addItem("Author", null, filterCommand);
		MenuItem filterAssignee = filterItem.addItem("Assignee", null, filterCommand);

		//centerLayout.addComponent(sideMenuLayout);
		//centerLayout.addComponent(sideMenuLayout, "left");
		
		/*
		HorizontalLayout leftAligner =  new HorizontalLayout();
		centerLayout.addComponent(leftAligner, "left");
		leftAligner.setSizeFull();
		leftAligner.addComponent(sideMenuLayout);

		leftAligner.setComponentAlignment(sideMenuLayout, Alignment.TOP_CENTER);
		*/

		Label lHeader = new Label("Issue Management Tool");
		lHeader.addStyleName(ValoTheme.LABEL_HUGE);
		lHeader.addStyleName(ValoTheme.TEXTAREA_ALIGN_CENTER);
		lHeader.setSizeFull();

		HorizontalLayout actions = new HorizontalLayout(filter, filterMenu, addNewBtn);
		VerticalLayout gridBoard = new VerticalLayout(lHeader, actions, grid);
		
		CustomLayout centerLayout = new CustomLayout("centerlayout");
		//HorizontalLayout centerLayout = new HorizontalLayout();
		centerLayout.setSizeFull();
		//centerLayout.addComponent(gridBoard);
		centerLayout.addComponent(gridBoard, "center");
		centerLayout.addComponent(firstButton, "p1");
		centerLayout.addComponent(secondButton, "p2");
		centerLayout.addComponent(thirdButton, "p3");

		//centerLayout.addComponent(rightmenu, "right");

		//Add the top menu buttons here.
		Button homeButton = new Button("Home");
		homeButton.setWidth("200px");
		homeButton.setHeight("100%");
		homeButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		Button issuesButton = new Button("Issues");
		issuesButton.setWidth("200px");
		issuesButton.setHeight("100%");
		issuesButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		Button chatButton = new Button("Chat");
		chatButton.setWidth("200px");
		chatButton.setHeight("100%");
		chatButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		Button membersButton = new Button("Members");
		membersButton.setWidth("200px");
		membersButton.setHeight("100%");
		membersButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);

		HorizontalLayout topMenuLayout = new HorizontalLayout();
		topMenuLayout.setHeight(80, Unit.PIXELS);
		topMenuLayout.addComponent(homeButton);
		topMenuLayout.addComponent(issuesButton);
		topMenuLayout.addComponent(chatButton);
		topMenuLayout.addComponent(membersButton);
		topMenuLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		topMenuLayout.setComponentAlignment(issuesButton, Alignment.MIDDLE_CENTER);
		topMenuLayout.setComponentAlignment(chatButton, Alignment.MIDDLE_CENTER);
		topMenuLayout.setComponentAlignment(membersButton, Alignment.MIDDLE_CENTER);

		Label lFooter = new Label("MET CS673 Software Engineer - Team A");
		lFooter.addStyleName(ValoTheme.LABEL_LIGHT);
		lFooter.addStyleName(ValoTheme.TEXTAREA_ALIGN_CENTER);
		lFooter.setSizeFull();

		//Add the menu buttons to the main layout.
		layout.addComponent(topMenuLayout, "menu");
		
		//layout.addComponent(lHeader, "top");
		layout.addComponent(centerLayout, "center");
		layout.addComponent(lFooter, "bottom");

		//Add the database here.
		grid.setHeight(450, Unit.PIXELS);
		grid.setWidth(900, Unit.PIXELS);

		// Initialize listing
		listTickets(null); 
	}

	// tag::listCustomers[]
	
	void listTickets(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems(repo.findAll());
		}
		else if (filterBy.equals("Project Name")) {
			grid.setItems(repo.findByProjectNameStartsWithIgnoreCase(filterText));
		}
		else if (filterBy.equals("Issue Title")) {
			grid.setItems(repo.findByIssueTitleStartsWithIgnoreCase(filterText));
		}
		else if (filterBy.equals("Author")) {
			grid.setItems(repo.findByAuthorStartsWithIgnoreCase(filterText));
		} 
		else if (filterBy.equals("Assignee")) {
			grid.setItems(repo.findByAssigneeStartsWithIgnoreCase(filterText));
		}
		grid.setColumns();
		grid.addColumn(Ticket::getProjectName).setCaption("Project name");
		grid.addColumn(Ticket::getIssueTitle).setCaption("Issue Title");
		grid.addColumn(Ticket::getAuthor).setCaption("Author");
		grid.addColumn(Ticket::getAssignee).setCaption("Assignee");
		grid.addColumn(Ticket::getDeadline).setCaption("Deadline");
	} 
	// end::listCustomers[]

}