package com.bow.hysterix.command.batching;

import com.bow.hysterix.service.BettingService;

/**
 * Object to wrap the parameters required by the method call {@link BettingService#getOddsForHorse}.
 */
public class GetOddsForHorseRequest {

	private final String raceCourseId;
	private final String horseId;
	
	public GetOddsForHorseRequest(String raceCourseId, String horseId){
		this.raceCourseId = raceCourseId;
		this.horseId = horseId;
	}
	
	public String getRaceCourseId() {
		return raceCourseId;
	}
	
	public String getHorseId() {
		return horseId;
	}
	
}
