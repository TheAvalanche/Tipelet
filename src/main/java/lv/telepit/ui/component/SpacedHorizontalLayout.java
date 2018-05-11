package lv.telepit.ui.component;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

public class SpacedHorizontalLayout extends HorizontalLayout {
	public SpacedHorizontalLayout() {
		setSpacing(true);
	}

	public SpacedHorizontalLayout(Component... children) {
		super(children);
		setSpacing(true);
	}
}
