package com.wesal.domain.core

internal object ApiUrl {
    const val CHECK_PHONE : String = "user/register-level1-add-mobile"
    const val RESEND_CODE_FOR_REG : String = "user/register-level2-resend-active-code"
    const val RESEND_CODE_FOR_FORGET : String = "user/forget-level2-resend-forget-code"
    const val CONFIRM_CODE_FOR_REG : String = "user/register-level2-check-active-code"
    const val CONFIRM_CODE_FOR_FORGET : String = "user/forget-level2-check-forget-code"
    const val REGISTER : String = "user/register-level3-add-details"
    const val CHECK_PHONE_FORGET_PASSWORD : String = "user/forget-level1-add-mobile"
    const val RESET_PASSWORD : String = "user/forget-level3-new-password"
    const val LOGIN : String = "user/login"
    const val HOME : String = "main-page"
    const val TOGGLE_ADVERT_FAVORITES : String = "advertisement-operations/add-remove-favorite"
    const val SUB_SECTIONS : String = "sub-sections/{id}"
    const val ADVERTS_IN_SECTIONS : String = "sub-section-advertisements/{id}"
    const val ADVERT_DETAILS : String = "show-advertisement/{id}"
    const val ADD_COMMENT : String = "advertisement-operations/add-comment"
    const val REMOVE_COMMENT : String = "advertisement-operations/remove-comment"

    const val ADD_RATE : String = "advertisement-operations/add-rate"
    const val REMOVE_RATE : String = "advertisement-operations/remove-rate"
    const val ADVERT_ON_MAP : String = "sub-section-map-advertisements/{id}"
    const val ADVERTER_PAGE : String = "advertiser-page/{id}"
    const val PACKAGES_LIST : String = "packages"
    const val BANKS_LIST : String = "all-bank-accounts"
    const val BANKS_INFO : String = "bank-account/{id}"
    const val PAY_PACKAGE : String = "user-add-advertisement/add-package"
    const val GET_MAIN_CATEGORIES : String = "main-sections/real_estate"
    const val GET_SUB_CATEGORIES : String = "sub-sections/{id}"

    //Create Advert Services
    const val CREATE_ADVERT_TERMS : String = "fixed-page/Terms-and-Conditions"
    const val CREATE_ADVERT_STEP_CHECK_TERMS : String = "user-add-advertisement/level2-check-package"
    const val CREATE_ADVERT_STEP_SECTION : String = "user-add-advertisement/level3-choose-sections"
    const val CREATE_ADVERT_STEP_MEDIA : String = "user-add-advertisement/level4-add-media"
    const val CREATE_ADVERT_STEP_FEATURES : String = "user-add-advertisement/level5-add-details"
    const val CREATE_ADVERT_STEP_CONTACT : String = "user-add-advertisement/level6-choose-contact-ways"
    const val CHECK_DRAFT_ADVERTS : String = "user-add-advertisement/level1-check-last-not-completed-advertisement"
    const val MAKE_NEW_ADVERT_ID : String = "user-add-advertisement/choose-continue-or-new"
    const val SIDES_LIST : String = "sides"
    const val BLOCKS_LIST : String = "blocks"
    const val ADVERT_FEATURES_LIST : String = "user-add-advertisement/show-advertisement-features"
    //Orders
    const val ORDERS_IN_SECTIONS : String = "sub-section-orders/{id}"
    const val CREATE_ORDER_STEP_CHECK : String = "user-add-order/level1-check-last-not-completed-order"
    const val CREATE_ORDER_STEP_GET_ID : String = "user-add-order/choose-continue-or-new"
    const val CREATE_ORDER_STEP_SECTION : String = "user-add-order/level2-choose-sections"
    const val GET_ORDER_SPECIFICATION : String = "user-add-order/show-order-features"
    const val CREATE_ORDER_STEP_SPECIFICATION : String = "user-add-order/level3-add-details"
    const val CREATE_ORDER_STEP_CONTACT : String = "user-add-order/level4-choose-contact-ways"
    const val GET_ORDER_DETAILS : String = "show-order/{id}"
    const val CREATE_COMMENT_FOR_ORDER : String = "order-operations/add-comment"
    const val REMOVE_COMMENT_FOR_ORDER : String = "order-operations/remove-comment"
    // Ask Hail
    const val GET_ASK_HAIL : String = "ask-hail"
    const val GET_ASK_HAIL_DETAILS : String = "show-question/{id}"
    const val ADD_COMMENT_TO_ASK : String = "question-operations/add-comment"
    const val REMOVE_COMMENT_TO_ASK : String = "question-operations/remove-comment"
    const val CREATE_ASK_HAIL : String = "user/add-question"
    //more
    const val GET_FIXED_PAGE : String = "all-fixed-pages"
    const val GET_FIXED_PAGE_DETAILS : String = "fixed-page/{slug}"
    const val GET_ABOUT_APP : String = "app-info"
    const val SEND_CONTACT_US : String = "contact-us"
    const val GET_SERVICE_LIST : String = "services/{type}"
    const val GET_SETTINGS : String = "edit-my-settings"
    const val CHANGE_SETTINGS : String = "update-my-settings"
    const val GET_JOBS_LIST : String = "jobs"
    const val APPLY_FOR_JOB : String = "job-apply"
    const val APPLY_FOR_SHOT : String = "real-estate-shot"
    const val GET_SPECIAL_ADVERTS : String = "all-special-advertisements"
    const val GET_FAV_ADVERT : String = "user/my-favorite-advertisements"
    const val CLEAR_FAV_ADVERT : String = "user/remove-all-favorite-advertisements"
    const val GET_MY_ADVERTS : String = "user/my-advertisements"


    //Create Advert Services
    const val CREATE_ADVERT_STEP_SOCIAL : String = "business-add-advertisement/level7-add-social-links"
    const val EDIT_ADVERT_SOCIAL : String = "business-update-advertisement/update-social-links"

    // edit advert
    const val EDIT_ADVERT_SECTIONS : String = "user-update-advertisement/update-sections"
    const val EDIT_ADVERT_MEDIA : String = "user-update-advertisement/update-media"
    const val EDIT_ADVERT_SPECIFICATION : String = "user-update-advertisement/update-details"
    const val EDIT_ADVERT_CONTACT : String = "user-update-advertisement/update-contact-ways"
    // makeAdvertSpecial
    const val GET_HIGHLIGHT_INFO : String = "show-special-information"
    const val GET_HIGHLIGHT_PACKAGES : String = "special-packages"
    const val PAY_HIGHLIGHT_PACKAGES : String = "advertisement-operations/make-special"
    //
    const val ACTIVE_OR_BLOCK_ADVERT : String = "user/active-or-block-advertisement"
    const val GET_INACTIVE_ADVERTS : String = "user/my-blocked-advertisements"
    const val ACTIVE_ALL_ADVERTS : String = "user/active-all-advertisements"
    const val DELETE_ADVERTS : String = "user/delete-advertisement"
    // My Orders
    const val GET_MY_ORDERS : String = "user/my-orders"
    const val DELETE_ORDER : String = "user/delete-order"


    const val GET_NOTIFICATIONS : String = "notification-operations/my-notifications"
    const val GET_UNREAD_NOTIFICATIONS : String = "notification-operations/my-unread-notifications"
    const val REMOVE_ALL_NOTIFICATIONS : String = "notification-operations/delete-all-notifications"
    const val REMOVE_ONE_NOTIFICATIONS : String = "notification-operations/delete-notification/{id}"
    // my questions
    const val GET_MY_QUESTIONS : String = "user/my-questions"
    const val REMOVE_QUESTION : String = "user/delete-question"
    const val EDIT_QUESTION : String = "user/update-question"
    ///////////////UPDATE INFO//////////////////////
    const val GET_USER_INFO : String = "edit-personal-data"
    const val UPDATE_USER_INFO : String = "update-personal-data"
    const val CHANGE_PASSWORD : String = "change-password"
    ///////////////My Packages//////////////////////
    const val GET_MY_PACKAGE : String = "my-packages"
    const val RENEW_MY_PACKAGE : String = "renew-package"
    const val UPDATE_MY_PACKAGE : String = "update-package"
    ///////////////Replays //////////////////////
    const val REPLAY_ON_QUESTION : String = "user/question-add-or-update-reply-on-comment"
    const val REPLAY_ON_ADVERT : String = "user/add-or-update-reply-on-comment"
    const val REPLAY_ON_ORDER : String = "user/order-add-or-update-reply-on-comment"
    ///////////////Chat //////////////////////
    const val GET_MESSAGES : String = "chat-operations/my-chats"
    const val GET_CHAT_HISTORY : String = "chat-operations/open-chat"
    ///////////////Chat //////////////////////
    const val EDIT_ORDER_SECTION : String = "user-update-order/update-sections"
    const val EDIT_ORDER_SPECIFICATION : String = "user-update-order/update-details"
    const val EDIT_ORDER_CONTACT : String = "user-update-order/update-contact-ways"
    //////////////////////////////////////////////////
    const val SPECIAL_ADVERT_PACKAGE_INFO : String = "advertisement-operations/show-special"
    /////////////////Comment List/////////////////////
    const val GET_COMMENTS_ADVERTS : String = "show-advertisement-comments/{id}"
    const val GET_RATES_ADVERTS : String = "show-advertisement-rates/{id}"
    const val GET_COMMENTS_ORDER : String = "show-order-comments/{id}"
    const val GET_COMMENTS_QUESTIONS : String = "show-question-comments/{id}"
    const val LOG_OUT : String = "logout"

    const val LAT_ON : String = "user/"


}