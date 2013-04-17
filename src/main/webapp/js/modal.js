function Modal(modalElement){
	this.element = $(modalElement);
	this.closeButton = $(modalElement).find(".modal-close-button");
	this.toggle();
	this.closeButton.off("click");
	this.closeButton.on("click", closeModal);
	var self = this;
	function closeModal(e){
		e.preventDefault();
		self.toggle();
	}
}
Modal.prototype.toggle = function(){
	this.element.toggleClass('hidden');
}
Modal.prototype.hide = function(){
	this.element.hide();
}
