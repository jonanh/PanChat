package panchat.data.models;

import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

import panchat.data.UserList;

public class UserTableModel extends AbstractTableModel implements Observer {

	private static final long serialVersionUID = 1L;

	private UserList userList;

	public UserTableModel(UserList userList) {
		this.userList = userList;
		this.userList.addObserver(this);
	}

	@Override
	public String getColumnName(int col) {
		switch (col) {
		case 0:
			return "Usuarios conectados";
		case 1:
			return "IP";
		case 2:
			return "Port";
		default:
			return "UUID";
		}
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public int getRowCount() {
		return userList.length();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return userList.getUser(rowIndex).nickName;
		case 1:
			return userList.getUser(rowIndex).ip;
		case 2:
			return userList.getUser(rowIndex).port;
		default:
			return userList.getUser(rowIndex).uuid;
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		this.fireTableDataChanged();
	}
}
