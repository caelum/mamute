<div class="subheader">
	<tags:userProfileLink user="${user}" htmlClass="title page-title" />
	<ul class="subheader-menu">
		<a href="${linkTo[UserProfileController].editProfile[user.id]}"><fmt:message key="user_profile.edit" /></a>
	</ul>
</div>
<div class="image-and-information">
	<img class="profile-image" src="${user.mediumPhoto}"/>
	<a href="#">atualizar foto</a>
</div>


<form class="validated-form profile-edit-form" action="${linkTo[UserProfileController].editProfile[user.id]}" method="POST">
	<span class="data-line">
		<label for="name" class="form-label"><fmt:message key="user_profile.edit.form.name.label" /></label>
		<input type="text" name="name" class="text-input required" maxlength="100" value="${user.name}"/>
	</span>
	<span class="data-line">
		<label for="email" class="form-label"><fmt:message key="user_profile.edit.form.email.label" /></label>
		<input type="email" name="email" class="required text-input email" maxlength="100" value="${user.email}"/>
	</span>
	<span class="data-line">
		<label for="realName" class="form-label"><fmt:message key="user_profile.edit.form.real_name.label" /></label>
		<input type="text" name="realName" class="text-input" maxlength="100" value="${user.realName}"/>
	</span>
	<span class="data-line">
		<label for="website" class="form-label"><fmt:message key="user_profile.edit.form.website.label" /></label>
		<input type="text" name="website" class="text-input url" maxlength="200" value="${user.website}"/>
	</span>
	<span class="data-line">
		<label for="birthDate" class="form-label"><fmt:message key="user_profile.edit.form.birth_date.label" /></label>
		<input type="text" name="birthDate" class="text-input date" maxlength="10" value="<tags:jodaTime pattern="dd/MM/YYYY" time="${user.birthDate}"></tags:jodaTime>" placeholder="dd/mm/yyyy"/>
	</span>
	<span class="data-line">
		<label for="location" class="form-label"><fmt:message key="user_profile.edit.form.location.label" /></label>
		<input type="text" name="location" class="text-input" maxlength="100" value="${user.location}"/>
	</span>
	<span class="data-line">
		<label for="description" class="form-label"><fmt:message key="user_profile.edit.form.about.label" /></label>
		<tags:markDown value="${user.about}" hintId="user-about-hint" />
	</span>
		
	<input type="submit" class="post-submit big-submit" value="<fmt:message key="user_profile.edit.form.submit" />"/>
	<tags:userProfileLink user="${user}" value="user_profile.edit.cancel" />
</form>