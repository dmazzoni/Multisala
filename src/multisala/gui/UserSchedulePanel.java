package multisala.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import multisala.core.Show;

public class UserSchedulePanel extends GuestSchedulePanel {

	private UserUI parent;
	
	public UserSchedulePanel(UserUI parent) {
		this();
		this.parent = parent;
		updateSchedule(0);
	}
	
	protected UserSchedulePanel() {
		super();
		schedule.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
					handleClick(e);
			}
		});
	}
	
	protected void handleClick(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
			int selection = schedule.convertRowIndexToModel(schedule.getSelectedRow());
			Show sh = ((ScheduleTableModel) schedule.getModel()).getShowAtIndex(selection);
			parent.tabbedView.add(new ReservationPanel(parent, sh));
		}
	}
}

