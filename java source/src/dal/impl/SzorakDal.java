package dal.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dal.ActionResult;
import dal.DataAccessLayer;
import dal.exceptions.CouldNotConnectException;
import dal.exceptions.EntityNotFoundException;
import dal.exceptions.NotConnectedException;
import model.Place;
import model.Person;
import model.PersonData;
import model.DiscoPeople;

/**
 * Initial implementation for the DataAccessLayer for the 23-SZORAK exercise.
 */
public class SzorakDal implements DataAccessLayer<Place, PersonData, DiscoPeople> {
	private Connection connection;
	protected static final String driverName = "oracle.jdbc.driver.OracleDriver";
	protected static final String databaseUrl = "jdbc:oracle:thin:@rapid.eik.bme.hu:1521:szglab";

	private void checkConnected() throws NotConnectedException {
		if (connection == null) {
			throw new NotConnectedException();
		}
	}

	@Override
	public void connect(String username, String password) throws CouldNotConnectException, ClassNotFoundException {
		try {
			if (connection == null || !connection.isValid(30)) {
				if (connection == null) {
					// Load the specified database driver
					Class.forName(driverName);
				} else {
					connection.close();
				}

				// Create new connection and get metadata
				connection = DriverManager.getConnection(databaseUrl, username, password);
			}
		} catch (SQLException e) {
			throw new CouldNotConnectException();
		}
	}

	@Override
	public List<Place> search(String keyword) throws NotConnectedException {
		checkConnected();
		try {
			ResultSet rs;
			switch(keyword) {
				case "":
					Statement stmt = connection.createStatement();
					stmt.executeQuery("SELECT * FROM places");
					rs = stmt.getResultSet();
					break;
				default:
					PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM places WHERE name = ?");
					preparedStatement.setString(1, "'%" + keyword + "%'");
					rs = preparedStatement.executeQuery();//stmt.getResultSet();
					break;
			}
			List<Place> retVal = new ArrayList<>();
			while(rs.next()) {
				Place place = new Place();
				place.setName(rs.getString("NAME"));
				place.setAddress(rs.getString("ADDRESS"));
				place.setPhone(rs.getString("PHONE"));
				retVal.add(place);
			}
			return retVal;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<DiscoPeople> getStatistics() throws NotConnectedException {
		checkConnected();
		return null;
	}

	@Override
	public boolean commit() throws NotConnectedException {
		checkConnected();
		return false;
	}

	@Override
	public boolean rollback() throws NotConnectedException {
		checkConnected();
		return false;
	}

	@Override
	public ActionResult insertOrUpdate(PersonData entity, Integer foreignKey)
			throws NotConnectedException, EntityNotFoundException {
		checkConnected();

		try {
			PreparedStatement query = connection.prepareStatement(
                    "SELECT PERSON_ID FROM PERSONS WHERE PERSON_ID = ?"
            );
			query.setInt(1, entity.getPerson_id());
			ResultSet resultSet = query.executeQuery();
			if(resultSet.next()){
				query = connection.prepareStatement(
						"UPDATE PERSONS " +
							 "SET NAME = ? , ADDRESS = ?, PHONE = ? , INCOME = ? , HOBBY = ? , FAVOURITE_MOVIE = ? " +
							 "WHERE PERSON_ID = ? "
				);
				query.setString(1, entity.getName());
				query.setString(2, entity.getAddress());
				query.setString(3, entity.getPhone());
				query.setInt(4, entity.getIncome());
				query.setString(5, entity.getHobby());
				query.setString(6, entity.getFavourite_movie());
				query.setInt(7, resultSet.getInt(1));
				query.executeUpdate();
				return ActionResult.UpdateOccurred;
			} else {
				query = connection.prepareStatement(
						"INSERT INTO PERSONS(PERSON_ID, NAME, ADDRESS, PHONE, INCOME, HOBBY, FAVOURITE_MOVIE) " +
								"VALUES( ? , ? , ? , ? , ? , ? , ? )"
				);
				query.setInt(1, entity.getPerson_id());
				query.setString(2, entity.getName());
				query.setString(3, entity.getAddress());
				query.setString(4, entity.getPhone());
				query.setInt(5, entity.getIncome());
				query.setString(6, entity.getHobby());
				query.setString(7, entity.getFavourite_movie());
				query.execute();
				return ActionResult.InsertOccurred;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return ActionResult.ErrorOccurred;
		}
	}

	@Override
	public boolean setAutoCommit(boolean value) throws NotConnectedException {
		checkConnected();
		return false;
	}

	@Override
	public void disconnect() {
		
	}

	@Override
	public List<Person> sampleQuery() throws NotConnectedException {
		checkConnected();
		List<Person> result = new ArrayList<>();
		try (Statement stmt = connection.createStatement()) {
			try (ResultSet rset = stmt.executeQuery("SELECT nev, szemelyi_szam FROM OKTATAS.SZEMELYEK "
					+ "ORDER BY NEV "
					+ "OFFSET 0 ROWS FETCH NEXT 20 ROWS ONLY")){
				while (rset.next()) {
					Person p = new Person(rset.getString("nev"), rset.getString("szemelyi_szam"));
					result.add(p);
				}
				return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
