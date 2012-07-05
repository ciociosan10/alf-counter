<import resource="classpath:alfresco/extension/templates/webscripts/com/tribloom/counter/counter.js">

function main() {
	var value = args.value;
	var name = url.templateArgs.name;
	var counterNode = getCounter(name);
	if (counterNode == null) {
		counterNode = countersFolder.createNode(name, "dm:counter");
	}
	if (counterNode.islocked || !counterNode.hasPermission("Write")) {
		status.setCode(
			status.STATUS_NOT_AUTHORIZED,
			"Node is locked or you do not have permission to access it"
		);
		return;
	}
	if (value != null) {
		value = parseInt(value);
		if (isNaN(value)) {
			status.setCode(
				status.STATUS_BAD_REQUEST,
				"Invalid value: " + url.templateArgs.value
			);
			return;
		}
	} else {
		value = 1 + counterNode.properties["dm:count"];
	}
	counterNode.properties["dm:count"] = value;
	counterNode.save();
	model.name = name;
	model.value = value;
}
main();