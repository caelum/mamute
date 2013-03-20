<tags:header title="metas.edit_profile.title"/>

<div class="subheader">
	<tags:userProfileLink user="${user}" htmlClass="title page-title" />
	<ul class="subheader-menu">
		<a href="${linkTo[UserProfileController].editProfile[user.id]}"><fmt:message key="user_profile.edit" /></a>
	</ul>
</div>
<div class="image-and-information">
	<img class="profile-image" src="${user.mediumPhoto}"/>
	<a href="https://br.gravatar.com/"><fmt:message key="user_profile.edit.photo" /></a>
</div>

<form class="validated-form profile-edit-form" action="${linkTo[UserProfileController].editProfile[user.id]}" method="POST">
	<label for="name" class="form-label"><fmt:message key="user_profile.edit.form.name.label" /></label>
	<input type="text" name="name" class="text-input required" minlength="4" maxlength="100" value="${user.name}"/>
	
	<label for="email" class="form-label"><fmt:message key="user_profile.edit.form.email.label" /></label>
	<input type="email" name="email" class="required text-input email" minlength="6" maxlength="100" value="${user.email}"/>
	
	<label for="realName" class="form-label"><fmt:message key="user_profile.edit.form.real_name.label" /></label>
	<input type="text" name="realName" class="text-input" maxlength="100" minlength="4" value="${user.realName}"/>
	
	<label for="website" class="form-label"><fmt:message key="user_profile.edit.form.website.label" /></label>
	<input type="text" name="website" class="text-input brutal-url" maxlength="200" value="${user.website}"/>
	
	<label for="birthDate" class="form-label"><fmt:message key="user_profile.edit.form.birth_date.label" /></label>
	<input type="text" name="birthDate" class="text-input date" maxlength="10" value="<tags:jodaTime pattern="dd/MM/YYYY" time="${user.birthDate}"></tags:jodaTime>" placeholder="dd/mm/yyyy"/>
	
	<label for="location" class="form-label"><fmt:message key="user_profile.edit.form.location.label" /></label>
	<input type="text" name="location" class="text-input" maxlength="100" value="${user.location}"/>
	
	<label for="description" class="form-label"><fmt:message key="user_profile.edit.form.about.label" /></label>
	<tags:markDown value="${user.about}" hintId="user-about-hint" minlength="6" maxlength="500" />
	
	<input type="submit" class="post-submit big-submit" value="<fmt:message key="user_profile.edit.form.submit" />"/>
	<tags:userProfileLink user="${user}" value="user_profile.edit.cancel" />
</form>