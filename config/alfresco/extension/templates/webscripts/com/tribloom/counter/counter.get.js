<import resource="classpath:alfresco/extension/templates/webscripts/com/tribloom/counter/counter.js">

function main() {
	var name = url.templateArgs.name;
	var counterNode =  getCounter(name);
	if (counterNode == null) {
		status.setCode(
			status.STATUS_NOT_FOUND, // 404
			"No counter found with the name " + name
		);
		return;
	}
	if (counterNode.hasPermission("Read")) {
		model.name = name;
		model.value = counterNode.properties["dm:count"];
	}
}
main();