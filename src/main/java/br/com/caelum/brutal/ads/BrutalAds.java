package br.com.caelum.brutal.ads;

import java.util.Random;

import javax.inject.Inject;

import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.User;

public class BrutalAds {
	
	private LoggedUser loggedUser;

	@Deprecated
	public BrutalAds() {
	}
	
	@Inject
	public BrutalAds(LoggedUser loggedUser) {
		this.loggedUser = loggedUser;
	}

	public boolean shouldShowAds() {
		if (!loggedUser.isLoggedIn()) 
			return true;
		User current = loggedUser.getCurrent();
		if(current.getKarma() <= 50) return true;
		if(current.getKarma() <= 1000) return shouldShowWithPercentage(50);
		return shouldShowWithPercentage(25);
	}

	private boolean shouldShowWithPercentage(int percentage) {
		return new Random().nextInt(101) <= percentage;
	}
	
	
}
