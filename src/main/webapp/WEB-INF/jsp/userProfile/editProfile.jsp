<div class="subheader">
	<h1 class="title page-title">${user.name}</h1>
	<ul class="subheader-menu">
		<a href="${linkTo[UserProfileController].editProfile[selectedUser.id]}">edit</a>
	</ul>
</div>
<div class="image-and-information">
	<img class="profile-image" src="${user.photo}?s=128"/>
	<a href="#">atualizar foto</a>
</div>