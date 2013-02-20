<div class="subheader">
	<tags:userProfileLink user="${user}" htmlClass="title page-title" />
	<ul class="subheader-menu">
		<a href="${linkTo[UserProfileController].editProfile[user.id]}">edit</a>
	</ul>
</div>
<div class="image-and-information">
	<img class="profile-image" src="${user.photo}?s=128"/>
	<a href="#">atualizar foto</a>
</div>

<form class="profile-edit-form" action="${linkTo[UserProfileController].editProfile[user.id]}" method="POST">
	<span class="data-line">
		<label for="email"><fmt:message key="user_profile.edit.form.email.label" /></label>
		<input type="email" name="email" class="text-input" value="${user.email}"/>
	</span>
	<span class="data-line">
		<label for="website"><fmt:message key="user_profile.edit.form.website.label" /></label>
		<input type="text" name="website" class="text-input" value="${user.website}"/>
	</span>
		<span class="data-line">
		<label for="name"><fmt:message key="user_profile.edit.form.name.label" /></label>
		<input type="text" name="name" class="text-input" value="${user.name}"/>
	</span>
	</span>
		<span class="data-line">
		<label for="birthDate"><fmt:message key="user_profile.edit.form.birth_date.label" /></label>
		<input type="dateTime" name="birthDate" class="text-input" /> <%-- value="${dateFormat.print(user.birthDate)}"/> --%>
	</span>
	</span>
		<span class="data-line">
		<label for="location"><fmt:message key="user_profile.edit.form.location.label" /></label>
		<input type="text" name="location" class="text-input" value="${user.location}"/>
	</span>

	<input type="submit" value="<fmt:message key="user_profile.edit.form.submit" />"/>
</form>