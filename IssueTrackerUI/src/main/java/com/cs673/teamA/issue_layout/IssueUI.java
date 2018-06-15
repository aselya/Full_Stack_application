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
		/* Layering layout
		 * vertical - label 
		 * 			- horizontal - label
		 *						 - vertical - horizontal (filter, button)
		 *						 			- grid view*/

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
		
		/*
		Button.ClickListener rightListener = new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				String capt = event.getButton().getCaption();
				String val = "empty";
				
				if(capt.equals("B01")){
					val = "Right Button one";
				}
				
				if(capt.equals("B02")){
					val = "Right Button two";
				}

				if(capt.equals("B03")){
					val = "Right Button three";
				}

				if(capt.equals("B04")){
					val = "Right Button four";
				}

				centerLabel.setValue(val);
				
			}
		};
		*/

		VerticalLayout leftmenu = new VerticalLayout();
		//leftmenu.setHeight(450, Unit.PIXELS);
		leftmenu.setHeightUndefined();
		Button firstButton = new Button("Project 1");
		firstButton.setWidth("200px");
		firstButton.addStyleName("primary");
		Button secondButton = new Button("Project 2");
		secondButton.setWidth("200px");
		secondButton.addStyleName("primary");
		Button thirdButton = new Button("Project 3");
		thirdButton.setWidth("200px");
		thirdButton.addStyleName("primary");
		leftmenu.addComponent(firstButton);
		leftmenu.addComponent(secondButton);
		leftmenu.addComponent(thirdButton);
		//leftmenu.setComponentAlignment(firstButton, Alignment.TOP_LEFT);
		//leftmenu.setComponentAlignment(secondButton, Alignment.TOP_LEFT);
		//leftmenu.setComponentAlignment(thirdButton, Alignment.TOP_LEFT);
		leftmenu.setSpacing(false);

		/*
		leftmenu.addStyleName(ValoTheme.TEXTAREA_ALIGN_CENTER);
		Button firstButton = new Button("Project 1");
		firstButton.setWidth("200px");
		firstButton.addStyleName("primary");
		firstButton.addClickListener(leftListener);
		leftmenu.addComponent((Component) firstButton, "first");
		Button secondButton = new Button("Project 2");
		secondButton.setWidth("200px");
		secondButton.addStyleName("primary");
		secondButton.addClickListener(leftListener);
		leftmenu.addComponent((Component) secondButton, "second");
		Button thirdButton = new Button("Project 3");
		thirdButton.setWidth("200px");
		thirdButton.addStyleName("primary");
		thirdButton.addClickListener(leftListener);
		leftmenu.addComponent((Component) thirdButton, "third");
		*/
		
		/* Don't use right menu.
		CustomLayout rightmenu = new CustomLayout("rightmenu");
		rightmenu.addStyleName(ValoTheme.TEXTAREA_ALIGN_CENTER);
		Button b01 = new Button("B01");
		b01.addStyleName("friendly");
		b01.addClickListener(rightListener);
		rightmenu.addComponent((Component) b01, "b01");
		Button b02 = new Button("B02");
		b02.addStyleName("friendly");
		b02.addClickListener(rightListener);
		rightmenu.addComponent((Component) b02, "b02");
		Button b03 = new Button("B03");
		b03.addStyleName("friendly");
		b03.addClickListener(rightListener);
		rightmenu.addComponent((Component) b03, "b03");
		Button b04 = new Button("B04");
		b04.addStyleName("friendly");
		b04.addClickListener(rightListener);
		rightmenu.addComponent((Component) b04, "b04"); */
		
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

		CustomLayout centerLayout = new CustomLayout("centerlayout");
		centerLayout.setSizeFull();

		VerticalLayout leftAligner =  new VerticalLayout();
		centerLayout.addComponent(leftAligner, "left");
		leftAligner.setHeight(450, Unit.PIXELS);
		leftAligner.addComponent(leftmenu);
		leftAligner.setComponentAlignment(leftmenu, Alignment.TOP_LEFT);

		HorizontalLayout actions = new HorizontalLayout(filter, filterMenu, addNewBtn);
		VerticalLayout gridBoard = new VerticalLayout(actions, grid);
		centerLayout.addComponent(gridBoard, "center");
		//centerLayout.addComponent(rightmenu, "right");

		Label lHeader = new Label("Issue Management Tool");
		lHeader.addStyleName(ValoTheme.LABEL_HUGE);
		lHeader.addStyleName(ValoTheme.TEXTAREA_ALIGN_CENTER);
		lHeader.setSizeFull();

		//Add the top menu buttons here.
		Button homeButton = new Button("Home");
		homeButton.setWidth("200px");
		homeButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		Button issuesButton = new Button("Issues");
		issuesButton.setWidth("200px");
		issuesButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		Button chatButton = new Button("Chat");
		chatButton.setWidth("200px");
		chatButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		Button membersButton = new Button("Members");
		membersButton.setWidth("200px");
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
		//leftAligner.setComponentAlignment(leftmenu, Alignment.TOP_LEFT);
		/*
		topMenuLayout.addComponent(homeButton, "home");
		topMenuLayout.addComponent(issuesButton, "issues");
		topMenuLayout.addComponent(chatButton, "chat");
		topMenuLayout.addComponent(membersButton, "members");
		*/

		Label lFooter = new Label("MET CS673 Software Engineer - Team A");
		lFooter.addStyleName(ValoTheme.LABEL_LIGHT);
		lFooter.addStyleName(ValoTheme.TEXTAREA_ALIGN_CENTER);
		lFooter.setSizeFull();

		//Add the menu buttons to the main layout.
		layout.addComponent(topMenuLayout, "menu");
		
		layout.addComponent(lHeader, "top");
		layout.addComponent(centerLayout, "center");
		layout.addComponent(lFooter, "bottom");

		//Add the database here.
		grid.setHeight(450, Unit.PIXELS);
		grid.setWidth(1000, Unit.PIXELS);
/*
		// Connect selected Customer to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			editor.editCustomer(e.getValue());
		});

		// Instantiate and edit new Customer the new button is clicked
		addNewBtn.addClickListener(e -> editor.editCustomer(new Customer("", "")));

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listCustomers(filter.getValue());
		});
*/
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