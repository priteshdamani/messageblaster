function startTour() {

// Instance the tour
    var tour = new Tour(
        {
            backdrop: true,
            storage: false
        }
    );

// Add your steps. Not too many, you don't really want to get your users sleepy
    tour.addSteps([
        {
            element: "#groupNumberInfo",
            title: "This is your private group number!",
            content: "You should send this to everyone you would like to join your group!"
        },
        {
            element: "#sideNav",
            title: "This is your main navigation bar.",
            content: "Here is where you pick your groups."
        },
        {
            element: "#startNewGroup",
            title: "Want to create a group?",
            content: "This is where you can create your own group."
        },
        {
            element: "#joinGroup",
            title: "Join an existing group?",
            content: "All you need is the group number!"
        },
        {
            element: "#dashboardMenu",
            title: "Dashboard",
            content: "This is where you can send messages quickly!"
        },
        {
            element: "#messagesMenu",
            title: "Messages",
            content: "Checkout all your messages here!"
        },
        {
            element: "#membersMenu",
            title: "Group Members",
            content: "See and remove group members here. You can also make them admins!"
        },
        {
            element: "#subscriptionMenu",
            title: "Subscription",
            content: "Check your current subscription status and upgrade here!"
        },
        {
            element: "#notificationOptionsMenu",
            title: "Notifications Options",
            content: "You can easily choose where you want to receive notifications for this group!"
        },
        {
            element: "#groupSettingsMenu",
            title: "Group Settings",
            content: "Change your group name and description."
        }
    ]);

// Initialize the tour
    tour.init();

// Start the tour
    tour.start();
}