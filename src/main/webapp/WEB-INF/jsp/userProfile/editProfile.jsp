<c:set var="title" value="${t['metas.edit_profile.title']}"/>
<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title}"/>

<div class="subheader">
	<tags:userProfileLink user="${user}" htmlClass="title page-title"/>
	<ul class="subheader-menu">
		<a href="${linkTo[UserProfileController].editProfile(user)}">${t['user_profile.edit']}</a>
	</ul>
</div>
<div class="image-and-information">
	<img class="profile-image" src="${user.getBigPhoto(env.get('gravatar.avatar.url'))}"/>
	<a href="${t['gravatar.url']}">${t['user_profile.edit.photo']}</a>
</div>

<form class="validated-form profile-edit-form" action="${linkTo[UserProfileController].editProfile(user)}" method="POST">
	<label for="name" class="form-label">${t['user_profile.edit.form.name.label']}</label>
	<input type="text" name="name" class="text-input required" minlength="4" maxlength="100" value="<c:out value="${user.name}" escapeXml="true"/>"/>
	
	<label for="email" class="form-label">${t['user_profile.edit.form.email.label']}</label>
	<input type="email" name="email" class="required text-input email" minlength="6" maxlength="100" value="<c:out value="${user.email}" escapeXml="true"/>"/>
	
	<label for="website" class="form-label">${t['user_profile.edit.form.website.label']}</label>
	<input type="text" name="website" class="text-input brutal-url" maxlength="200" value="<c:out value="${user.website}" escapeXml="true"/>"/>
	
	<label for="birthDate" class="form-label">${t['user_profile.edit.form.birth_date.label']}</label>
	<input type="text" name="birthDate" id="datepicker-age" class="text-input date" maxlength="10" value="<tags:jodaTime pattern="dd/MM/YYYY" time="${user.birthDate}"/>" placeholder="dd/mm/yyyy"/>
	
	<label for="location" class="form-label">${t['user_profile.edit.form.location.label']}</label>
	<input type="text" name="location" class="text-input" maxlength="100" value="<c:out value="${user.location}" escapeXml="true"/>"/>
	
	<label for="description" class="form-label">${t['user_profile.edit.form.about.label']}</label>
	<tags:markDown value="${user.about}" hintId="user-about-hint" minlength="6" maxlength="500" />

	<input type="checkbox" name="isSubscribed" class="text-input inline-box" maxlength="100" ${user.subscribedToNewsletter? 'checked':''} />
	<label for="isSubscribed" class="form-label inline-label">${t['user_profile.edit.form.subscribe.label']}</label>
	<input type="submit" class="post-submit big-submit submit" value="${t['user_profile.edit.form.submit']}"/>
	<tags:userProfileLink user="${user}" value="user_profile.edit.cancel"/>
</form>