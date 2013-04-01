package br.com.caelum.brutal.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.caelum.brutal.model.SubscribableDTO;
import br.com.caelum.brutal.model.User;

public class SubscribableEmail {

	private final List<SubscribableDTO> subscribables;

	private SubscribableEmail(List<SubscribableDTO> subscribables) {
		this.subscribables = subscribables;
	}
	
	static List<SubscribableEmail> buildSubscribableEmails(List<SubscribableDTO> dtos) {
		return new SubscribableEmailFactory().getSubscribableEmails(dtos);
	}

	public User getUser() {
		return subscribables.get(0).getUser();
	}

	public List<SubscribableDTO> getSubscribables() {
		return subscribables;
	}
	
	
	private static class SubscribableEmailFactory {
		
		public List<SubscribableEmail> getSubscribableEmails(List<SubscribableDTO> subscribables){
			List<SubscribableEmail> subscribableEmails = new ArrayList<>();
			Map<String, List<SubscribableDTO>> emailAndSubscribableMap = groupByUserEmail(subscribables);
			for (Entry<String, List<SubscribableDTO>> entry : emailAndSubscribableMap.entrySet()) {
				subscribableEmails.add(new SubscribableEmail(entry.getValue()));
			}
			return subscribableEmails;
		}

		private Map<String, List<SubscribableDTO>> groupByUserEmail(List<SubscribableDTO> recentSubscribables) {
			Map<String, List<SubscribableDTO>> subscribablesByEmail = new HashMap<>(); 
			for (SubscribableDTO subscribableDTO : recentSubscribables) {
				User user = subscribableDTO.getUser();
				String userEmail = user.getEmail();
				List<SubscribableDTO> subscribables = subscribablesByEmail.get(userEmail);
				if (subscribables == null) {
					subscribables = new ArrayList<>();
				}
				subscribables.add(subscribableDTO);
				subscribablesByEmail.put(userEmail, subscribables);
			}
			return subscribablesByEmail;
		}
	}
	
	

}
