/**
 * 
 */
package mx.com.muxiqa.services.location.cloudmade.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mx.com.muxiqa.services.core.db.entities.Location;

/**
 * @author gerardomartinezgil
 * 
 */
public final class CloudMadeRoute implements Serializable {
	private static final long serialVersionUID = 1L;

	private String distance;
	private String time;
	private String start;
	private String end;
	private List<Location> routePoints;
	private List<Location> routeDirections;

	public CloudMadeRoute() {
		routePoints = new ArrayList<Location>();
		routeDirections = new ArrayList<Location>();
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public void setRoutePoints(List<Location> routePoints) {
		this.routePoints = routePoints;
	}

	public void setRouteDirections(List<Location> routeDirections) {
		this.routeDirections = routeDirections;
	}

	public void addRouteDirection(Location location) {
		routeDirections.add(location);
	}

	public List<Location> getRouteDirections() {
		return routeDirections;
	}

	public List<Location> getRoutePoints() {
		return routePoints;
	}

	public void addRoutePoint(Location location) {
		routePoints.add(location);
	}

}