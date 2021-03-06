package com.qtech.saman.ui.activities;

import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PoliciesActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.button_done)
    Button buttonDone;
    @BindView(R.id.checkbox)
    CheckBox checkbox;

    int type = 0;
    boolean checkRequired = false;

    String show = "";
    String terms = "<p>OVERVIEW</p>\n" +
            "<p><br />This mobile application is operated by Saman. Throughout the app, the terms &ldquo;we&rdquo;, &ldquo;us&rdquo; and &ldquo;our&rdquo; refer to Saman. Saman offers this application, including all information, tools and services available from this site to you, the user, conditioned upon your acceptance of all terms, conditions, policies and notices stated here.</p>\n" +
            "<p><br />By visiting our mobile application and/ or purchasing something from us, you engage in our &ldquo;Service&rdquo; and agree to be bound by the following terms and conditions (&ldquo;Terms of Service&rdquo;, &ldquo;Terms&rdquo;), including those additional terms and conditions and policies referenced herein and/or available by hyperlink. These Terms of Service apply to all users of the site, including without limitation users who are browsers, vendors, customers, merchants, and/ or contributors of content.</p>\n" +
            "<p><br />Please read these Terms of Service carefully before accessing or using our app. By accessing or using any part of the app, you agree to be bound by these Terms of Service. If you do not agree to all the terms and conditions of this agreement, then you may not access the mobile application or use any services. If these Terms of Service are considered an offer, acceptance is expressly limited to these Terms of Service.</p>\n" +
            "<p><br />Any new features or tools which are added to the current store shall also be subject to the Terms of Service. You can review the most current version of the Terms of Service at any time on this page. We reserve the right to update, change or replace any part of these Terms of Service by posting updates and/or changes to our application. It is your responsibility to check this page periodically for changes. Your continued use of or access to the application following the posting of any changes constitutes acceptance of those changes.</p>\n" +
            "<p><br />Our store is hosted on Qtech. They provide us with the online e-commerce platform that allows us to sell our products and services to you.<br />&nbsp;<br />&nbsp;<br />&nbsp;</p>\n" +
            "<p><strong>SECTION 1 - ONLINE STORE TERMS</strong></p>\n" +
            "<p><br />By agreeing to these Terms of Service, you represent that you are at least the age of majority in your region, or that you are the age of majority in your region and you have given us your consent to allow any of your minor dependents to use this application</p>\n" +
            "<p><br />You may not use our products for any illegal or unauthorized purpose nor may you, in the use of the Service, violate any laws in your jurisdiction (including but not limited to copyright laws).</p>\n" +
            "<p><br />You must not transmit any worms or viruses or any code of a destructive nature.<br />A breach or violation of any of the Terms will result in an immediate termination of your Services.</p>\n" +
            "<p>&nbsp;<br /><strong>SECTION 2 - GENERAL CONDITIONS</strong></p>\n" +
            "<p><br />We reserve the right to refuse service to anyone for any reason at any time.<br />You understand that your content (not including credit card information), may be transferred unencrypted and involve (a) transmissions over various networks; and (b) changes to conform and adapt to technical requirements of connecting networks or devices. Credit card information is always encrypted during transfer over networks.</p>\n" +
            "<p><br />You agree not to reproduce, duplicate, copy, sell, resell or exploit any portion of the Service, use of the Service, or access to the Service or any contact on the applicaiton through which the service is provided, without express written permission by us.</p>\n" +
            "<p><br />The headings used in this agreement are included for convenience only and will not limit or otherwise affect these Terms.</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>SECTION 3 - ACCURACY, COMPLETENESS AND TIMELINESS OF INFORMATION</strong></p>\n" +
            "<p><br />We are not responsible if information made available on this site is not accurate, complete or current. The material on this site is provided for general information only and should not be relied upon or used as the sole basis for making decisions without consulting primary, more accurate, more complete or more timely sources of information. Any reliance on the material on this site is at your own risk.</p>\n" +
            "<p><br />This site may contain certain historical information. Historical information, necessarily, is not current and is provided for your reference only. We reserve the right to modify the contents of this site at any time, but we have no obligation to update any information on our site. You agree that it is your responsibility to monitor changes to our site.</p>\n" +
            "<p><br /><strong>SECTION 4 - MODIFICATIONS TO THE SERVICE AND PRICES</strong></p>\n" +
            "<p><br />Prices for our products are subject to change without notice.<br />We reserve the right at any time to modify or discontinue the Service (or any part or content thereof) without notice at any time.</p>\n" +
            "<p><br />We shall not be liable to you or to any third-party for any modification, price change, suspension or discontinuance of the Service.</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>SECTION 5 - PRODUCTS OR SERVICES (if applicable)</strong></p>\n" +
            "<p><br />Certain products or services may be available exclusively online through the application. These products or services may have limited quantities and are subject to return or exchange only according to our Return Policy.</p>\n" +
            "<p><br />We have made every effort to display as accurately as possible the colors and images of our products that appear at the store. We cannot guarantee that your computer monitor's display of any color will be accurate.</p>\n" +
            "<p><br />We reserve the right, but are not obligated, to limit the sales of our products or Services to any person, geographic region or jurisdiction. We may exercise this right on a case-by-case basis. We reserve the right to limit the quantities of any products or services that we offer. All descriptions of products or product pricing are subject to change at any time without notice, at the sole discretion of us. We reserve the right to discontinue any product at any time. Any offer for any product or service made on this site is void where prohibited.</p>\n" +
            "<p><br />We do not warrant that the quality of any products, services, information, or other material purchased or obtained by you will meet your expectations, or that any errors in the Service will be corrected.</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>SECTION 6 - ACCURACY OF BILLING AND ACCOUNT INFORMATION</strong></p>\n" +
            "<p><br />We reserve the right to refuse any order you place with us. We may, in our sole discretion, limit or cancel quantities purchased per person, per household or per order. These restrictions may include orders placed by or under the same customer account, the same credit card, and/or orders that use the same billing and/or shipping address. In the event that we make a change to or cancel an order, we may attempt to notify you by contacting the e-mail and/or billing address/phone number provided at the time the order was made. We reserve the right to limit or prohibit orders that, in our sole judgment, appear to be placed by dealers, resellers or distributors.</p>\n" +
            "<p><br />You agree to provide current, complete and accurate purchase and account information for all purchases made at our store. You agree to promptly update your account and other information, including your email address and credit card numbers and expiration dates, so that we can complete your transactions and contact you as needed.</p>\n" +
            "<p><br />For more detail, please review our Returns Policy.</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>SECTION 7 - OPTIONAL TOOLS</strong></p>\n" +
            "<p><br />We may provide you with access to third-party tools over which we neither monitor nor have any control nor input.</p>\n" +
            "<p><br />You acknowledge and agree that we provide access to such tools &rdquo;as is&rdquo; and &ldquo;as available&rdquo; without any warranties, representations or conditions of any kind and without any endorsement. We shall have no liability whatsoever arising from or relating to your use of optional third-party tools.</p>\n" +
            "<p><br />Any use by you of optional tools offered through the site is entirely at your own risk and discretion and you should ensure that you are familiar with and approve of the terms on which tools are provided by the relevant third-party provider(s).<br />We may also, in the future, offer new services and/or features through the application (including, the release of new tools and resources). Such new features and/or services shall also be subject to these Terms of Service.</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>SECTION 8 - THIRD-PARTY LINKS</strong></p>\n" +
            "<p><br />Certain content, products and services available via our Service may include materials from third parties.</p>\n" +
            "<p><br />Third-party links on this site may direct you to third-party websites/application that are not affiliated with us. We are not responsible for examining or evaluating the content or accuracy and we do not warrant and will not have any liability or responsibility for any third-party materials or websites/application, or for any other materials, products, or services of third parties.</p>\n" +
            "<p><br />We are not liable for any harm or damages related to the purchase or use of goods, services, resources, content, or any other transactions made in connection with any third-party websites/application. Please review carefully the third-party's policies and practices and make sure you understand them before you engage in any transaction. Complaints, claims, concerns, or questions regarding third-party products should be directed to the third-party.</p>\n" +
            "<p><br /><strong>SECTION 9 - USER COMMENTS, FEEDBACK AND OTHER SUBMISSIONS</strong></p>\n" +
            "<p><br />If, at our request, you send certain specific submissions (for example contest entries) or without a request from us you send creative ideas, suggestions, proposals, plans, or other materials, whether online, by email, by postal mail, or otherwise (collectively, 'comments'), you agree that we may, at any time, without restriction, edit, copy, publish, distribute, translate and otherwise use in any medium any comments that you forward to us. We are and shall be under no obligation (1) to maintain any comments in confidence; (2) to pay compensation for any comments; or (3) to respond to any comments.</p>\n" +
            "<p><br />We may, but have no obligation to, monitor, edit or remove content that we determine in our sole discretion are unlawful, offensive, threatening, libelous, defamatory, pornographic, obscene or otherwise objectionable or violates any party&rsquo;s intellectual property or these Terms of Service.</p>\n" +
            "<p><br />You agree that your comments will not violate any right of any third-party, including copyright, trademark, privacy, personality or other personal or proprietary right. You further agree that your comments will not contain libelous or otherwise unlawful, abusive or obscene material, or contain any computer virus or other malware that could in any way affect the operation of the Service or any related website/application. You may not use a false e-mail address, pretend to be someone other than yourself, or otherwise mislead us or third-parties as to the origin of any comments. You are solely responsible for any comments you make and their accuracy. We take no responsibility and assume no liability for any comments posted by you or any third-party.</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>SECTION 10 - PERSONAL INFORMATION</strong></p>\n" +
            "<p><br />Your submission of personal information through the store is governed by our Privacy Policy. To view our Privacy Policy.</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>SECTION 11 - ERRORS, INACCURACIES AND OMISSIONS</strong></p>\n" +
            "<p><br />Occasionally there may be information on our site or in the Service that contains typographical errors, inaccuracies or omissions that may relate to product descriptions, pricing, promotions, offers, product shipping charges, transit times and availability. We reserve the right to correct any errors, inaccuracies or omissions, and to change or update information or cancel orders if any information in the Service or on any related website/application is inaccurate at any time without prior notice (including after you have submitted your order).<br />We undertake no obligation to update, amend or clarify information in the Service or on any related website/application, including without limitation, pricing information, except as required by law. No specified update or refresh date applied in the Service or on any related website or application, should be taken to indicate that all information in the Service or on any related website/application has been modified or updated.</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>SECTION 12 - PROHIBITED USES</strong></p>\n" +
            "<p><br />In addition to other prohibitions as set forth in the Terms of Service, you are prohibited from using the application or its content: (a) for any unlawful purpose; (b) to solicit others to perform or participate in any unlawful acts; (c) to violate any international, federal, provincial or state regulations, rules, laws, or local ordinances; (d) to infringe upon or violate our intellectual property rights or the intellectual property rights of others; (e) to harass, abuse, insult, harm, defame, slander, disparage, intimidate, or discriminate based on gender, sexual orientation, religion, ethnicity, race, age, national origin, or disability; (f) to submit false or misleading information; (g) to upload or transmit viruses or any other type of malicious code that will or may be used in any way that will affect the functionality or operation of the Service or of any related website/application, other websites/application, or the Internet; (h) to collect or track the personal information of others; (i) to spam, phish, pharm, pretext, spider, crawl, or scrape; (j) for any obscene or immoral purpose; or (k) to interfere with or circumvent the security features of the Service or any related website/application, other websites/ application, or the Internet. We reserve the right to terminate your use of the Service or any related website/application for violating any of the prohibited uses.</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>SECTION 13 - DISCLAIMER OF WARRANTIES; LIMITATION OF LIABILITY</strong></p>\n" +
            "<p><br />We do not guarantee, represent or warrant that your use of our service will be uninterrupted, timely, secure or error-free.</p>\n" +
            "<p><br />We do not warrant that the results that may be obtained from the use of the service will be accurate or reliable.</p>\n" +
            "<p><br />You agree that from time to time we may remove the service for indefinite periods of time or cancel the service at any time, without notice to you.</p>\n" +
            "<p><br />You expressly agree that your use of, or inability to use, the service is at your sole risk. The service and all products and services delivered to you through the service are (except as expressly stated by us) provided 'as is' and 'as available' for your use, without any representation, warranties or conditions of any kind, either express or implied, including all implied warranties or conditions of merchantability, merchantable quality, fitness for a particular purpose, durability, title, and non-infringement.</p>\n" +
            "<p><br />In no case shall Saman, our directors, officers, employees, affiliates, agents, contractors, interns, suppliers, service providers or licensors be liable for any injury, loss, claim, or any direct, indirect, incidental, punitive, special, or consequential damages of any kind, including, without limitation lost profits, lost revenue, lost savings, loss of data, replacement costs, or any similar damages, whether based in contract, tort (including negligence), strict liability or otherwise, arising from your use of any of the service or any products procured using the service, or for any other claim related in any way to your use of the service or any product, including, but not limited to, any errors or omissions in any content, or any loss or damage of any kind incurred as a result of the use of the service or any content (or product) posted, transmitted, or otherwise made available via the service, even if advised of their possibility. Because some states or jurisdictions do not allow the exclusion or the limitation of liability for consequential or incidental damages, in such states or jurisdictions, our liability shall be limited to the maximum extent permitted by law.</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>SECTION 14 - INDEMNIFICATION</strong></p>\n" +
            "<p><br />You agree to indemnify, defend and hold harmless Saman and our parent, subsidiaries, affiliates, partners, officers, directors, agents, contractors, licensors, service providers, subcontractors, suppliers, interns and employees, harmless from any claim or demand, including reasonable attorneys&rsquo; fees, made by any third-party due to or arising out of your breach of these Terms of Service or the documents they incorporate by reference, or your violation of any law or the rights of a third-party.</p>\n" +
            "<p>\f</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><br /><strong>SECTION 15 - SEVERABILITY</strong></p>\n" +
            "<p><br />In the event that any provision of these Terms of Service is determined to be unlawful, void or unenforceable, such provision shall nonetheless be enforceable to the fullest extent permitted by applicable law, and the unenforceable portion shall be deemed to be severed from these Terms of Service, such determination shall not affect the validity and enforceability of any other remaining provisions.</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>SECTION 16 - TERMINATION</strong></p>\n" +
            "<p><br />The obligations and liabilities of the parties incurred prior to the termination date shall survive the termination of this agreement for all purposes.</p>\n" +
            "<p><br />These Terms of Service are effective unless and until terminated by either you or us. You may terminate these Terms of Service at any time by notifying us that you no longer wish to use our Services, or when you cease using our site.</p>\n" +
            "<p><br />If in our sole judgment you fail, or we suspect that you have failed, to comply with any term or provision of these Terms of Service, we also may terminate this agreement at any time without notice and you will remain liable for all amounts due up to and including the date of termination; and/or accordingly may deny you access to our Services (or any part thereof)</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>SECTION 17 - ENTIRE AGREEMENT</strong></p>\n" +
            "<p><br />The failure of us to exercise or enforce any right or provision of these Terms of Service shall not constitute a waiver of such right or provision.</p>\n" +
            "<p><br />These Terms of Service and any policies or operating rules posted by us on this site or in respect to The Service constitutes the entire agreement and understanding between you and us and govern your use of the Service, superseding any prior or contemporaneous agreements, communications and proposals, whether oral or written, between you and us (including, but not limited to, any prior versions of the Terms of Service).</p>\n" +
            "<p><br />Any ambiguities in the interpretation of these Terms of Service shall not be construed against the drafting party.<br />&nbsp;<br />&nbsp;<br />\f<br /><strong>SECTION 18 - GOVERNING LA</strong></p>\n" +
            "<p><br />These Terms of Service and any separate agreements whereby we provide you Services shall be governed by and construed in accordance with the laws of Sultanate of Oman</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>SECTION 19 - CHANGES TO TERMS OF SERVICE</strong></p>\n" +
            "<p><br />You can review the most current version of the Terms of Service at any time at this page.</p>\n" +
            "<p><br />We reserve the right, at our sole discretion, to update, change or replace any part of these Terms of Service by posting updates and changes to our application. It is your responsibility to check our application periodically for changes. Your continued use of or access to our application or the Service following the posting of any changes to these Terms of Service constitutes acceptance of those changes.</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>SECTION 20 - CONTACT INFORMATION</strong></p>\n" +
            "<p><br />Questions about the Terms of Service should be sent to us at saman@saman.om.</p>";


    @OnClick(R.id.toolbar_back)
    public void back() {
        super.onBackPressed();
    }

    String privacy = "<p>This Privacy Policy describes how your personal information is collected, used, and shared when you visit or make a purchase from SAMAN MOBILE APP</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>PERSONAL INFORMATION WE COLLECT</strong></p>\n" +
            "<p><br />When you visit the APP, we automatically collect certain information about your device, including information  <span >about your web browser, IP address, time zone, and some of the cookies that are installed on your device.</span> Additionally, as you browse the APP, we collect information about the individual pages or products that you view, what app or search terms referred you to the Site, and information about how you interact with the application. We refer to this automatically-collected information as &ldquo;Device Information.&rdquo;</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p>We collect Device Information using the following technologies:</p>\n" +
            "<ul>\n" +
            "<li><span >&ldquo;Cookies&rdquo; are data files that are placed on your device or computer and often include an anonymous unique identifier. For more information about cookies, and how to disable cookies, visit</span> <span style=\"color: #0000EE;\"><a style=\"color: #0000EE;\" href=\"http://www.allaboutcookies.org\">http://www.allaboutcookies.org</a></span><span style=\"color: #0000EE;\">.</span>&nbsp; &nbsp;</li>\n" +
            "<li><span >&ldquo;Log files&rdquo; track actions occurring on the Site, and collect data including your IP address, browser type, Internet service provider, referring/exit pages, and date/time stamps.</span></li>\n" +
            "<li><span >&ldquo;Web beacons,&rdquo; &ldquo;tags,&rdquo; and &ldquo;pixels&rdquo; are electronic files used to record information about how you browse the Site.</span></li>\n" +
            "</ul>" +
            "<p><br />Additionally, when you make a purchase or attempt to make a purchase through the application, we collect certain information from you, including your name, billing address, shipping address, payment information (including credit card or debit card numbers, email address, and phone number. We refer to this information as &ldquo;Order Information.&rdquo;</p>\n" +
            "<p><br />When we talk about &ldquo;Personal Information&rdquo; in this Privacy Policy, we are talking both about Device Information and Order Information.</p>\n" +
            "<p><strong>HOW DO WE USE YOUR PERSONAL INFORMATION?</strong></p>\n" +
            "<p><br />We use the Order Information that we collect generally to fulfill any orders placed through the application (including processing your payment information, arranging for shipping, and providing you with invoices and/or order confirmations). Additionally, we use this Order Information to:</p>\n" +
            "<p><br />Communicate with you;</p>\n" +
            "<p><br />Screen our orders for potential risk or fraud; and when in line with the preferences you have shared with us, provide you with information or advertising relating to our products or services.</p>\n" +
            "<p><br />We use the Device Information that we collect to help us screen for potential risk and fraud (in particular, your IP address), and more generally to improve and optimize our Site (for example, by generating analytics about how our customers browse and interact with the Site, and to assess the success of our marketing and advertising campaigns).</p>\n" +
            "<p><br /><strong>SHARING YOUR PERSONAL INFORMATION</strong></p>\n" +
            "<p><br />We share your Personal Information with third parties to help us use your Personal Information, as described above. For example, we use Google Analytics to help us understand how our customers use the Site/APP--you can read more about how Google uses your Personal Information here: <a href=\"https://www.google.com/intl/en/policies/privacy/\">https://www.google.com/intl/en/policies/privacy/</a>.&nbsp; You can also opt-out of Google Analytics here:&nbsp; <a href=\"https://tools.google.com/dlpage/gaoptout\">https://tools.google.com/dlpage/gaoptout</a> .</p>\n" +
            "<p><br />Finally, we may also share your Personal Information to comply with applicable laws and regulations, to respond to a subpoena, search warrant or other lawful request for information we receive, or to otherwise protect our rights.</p>\n" +
            "<p><br /><strong>BEHAVIOURAL ADVERTISING</strong></p>\n" +
            "<p><br />As described above, we use your Personal Information to provide you with targeted advertisements or marketing communications we believe may be of interest to you. For more information about how targeted advertising works, you can visit the Network Advertising Initiative&rsquo;s (&ldquo;NAI&rdquo;) educational page at http://www.networkadvertising.org/understanding-online-advertising/how-does-it-work.</p>\n" +
            "<p><strong>DO NOT TRACK</strong></p>\n" +
            "<p><br />Please note that we do not alter our Application&rsquo;s data collection and use practices when we see a Do Not Track signal from your browser.</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>DATA RETENTION</strong></p>\n" +
            "<p><br />When you place an order through the Site, we will maintain your Order Information for our records unless and until you ask us to delete this information.</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>MINORS</strong></p>\n" +
            "<p><br />The Site is not intended for individuals under the age of 14.</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>CHANGES</strong></p>\n" +
            "<p><br />We may update this privacy policy from time to time in order to reflect, for example, changes to our practices or for other operational, legal or regulatory reasons.</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>CONTACT US</strong></p>\n" +
            "<p><br />For more information about our privacy practices, if you have questions, or if you would like to make a complaint, please contact us by e-mail at saman@saman.om or by mail using the details provided below:</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p>&nbsp;</p>";
    String returnPolicy = "<p>Our policy lasts 15 days. If 15 days have gone by since your purchase, unfortunately we can&rsquo;t offer you a refund or exchange. To be eligible for a return, your item must be unused and in the same condition that you received it. It must also be in the original packaging. Several types of goods are exempt from being returned. Perishable goods such as food, flowers, newspapers or magazines cannot be returned. We also do not accept products that are intimate or sanitary goods, hazardous materials, or flammable liquids or gases.</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><br />Additional non-returnable items:</p>\n" +
            "<ul>\n" +
            "<li>Gift cards</li>\n" +
            "<li>Downloadable software products</li>\n" +
            "<li>Some health and personal care items</li>\n" +
            "<li>To complete your return, we require a receipt or proof of purchase. There are certain situations where only partial refunds are granted: (if applicable)</li>\n" +
            "<li>Book with obvious signs of use</li>\n" +
            "<li>CD, DVD, VHS tape, software, video game, cassette tape, or vinyl record that has been opened.</li>\n" +
            "<li>Any item not in its original condition, is damaged or missing parts for reasons not due to our error.</li>\n" +
            "<li>Any item that is returned more than 15 days after delivery</li>\n" +
            "</ul>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>Refunds (if applicable)</strong></p>\n" +
            "<p><br />Once your return is received and inspected, we will send you an email to notify you that we have received your returned item. We will also notify you of the approval or rejection of your refund.</p>\n" +
            "<p><br />If you are approved, then your refund will be processed, and a credit will automatically be applied to your credit card or original method of payment, within a certain amount of days.</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>Late or missing refunds (if applicable)</strong></p>\n" +
            "<p><br />If you haven&rsquo;t received a refund yet, first check your bank account again. Then contact your credit card company, it may take some time before your refund is officially posted. Next contact your bank. There is often some processing time before a refund is posted. If you&rsquo;ve done all of this and you still have not received your refund yet, please contact us at <a href=\"mailto:Saman@saman.om\">Saman@saman.om</a>.</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>Sale items (if applicable)</strong></p>\n" +
            "<p><br />Only regular priced items may be refunded, unfortunately sale items cannot be refunded.</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>Exchanges (if applicable)</strong></p>\n" +
            "<p><br />We only replace items if they are defective or damaged. If you need to exchange it for the same item, send us an email at Saman@saman.om and send your item to:  <span >Muscat, Muscat, MA, 115, Oman.</span></p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>Gifts</strong></p>\n" +
            "<p><br />If the item was marked as a gift when purchased and shipped directly to you, you&rsquo;ll receive a gift credit for the value of your return. Once the returned item is received, a gift certificate will be mailed to you.</p>\n" +
            "<p><br />If the item wasn&rsquo;t marked as a gift when purchased, or the gift giver had the order shipped to themselves to give to you later, we will send a refund to the gift giver and he will find out about your return.</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p><strong>Shipping</strong></p>\n" +
            "<p><br />To return your product, you should mail your product to <span >Muscat, Muscat, MA, 115, Oman.</span></p>\n" +
            "<p><br />You will be responsible for paying for your own shipping costs for returning your item. Shipping costs are non-refundable. If you receive a refund, the cost of return shipping will be deducted from your refund.</p>\n" +
            "<p><br />Depending on where you live, the time it may take for your exchanged product to reach you, may vary.</p>\n" +
            "<p><br />If you are shipping an item over 30 OMR, you should consider using a trackable shipping service or purchasing shipping insurance. We don&rsquo;t guarantee that we will receive your returned item.</p>\n" +
            "<p>&nbsp;</p>\n" +
            "<p>&nbsp;</p>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 0);

        if (getIntent().hasExtra("checkRequired")) {
            checkRequired = getIntent().getBooleanExtra("checkRequired", false);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (type == 0) {
            show = privacy;
            toolbarTitle.setText(getString(R.string.privacy_policy));
        } else if (type == 1) {
            show = terms;
            toolbarTitle.setText(getString(R.string.term_of_use));
        } else if (type == 2) {
            show = returnPolicy;
            toolbarTitle.setText(getString(R.string.return_policy));
        }
        toolbarBack.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarBack.setImageDrawable(getDrawable(R.drawable.ic_back));
        } else {
            toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            text.setText(Html.fromHtml(show, Html.FROM_HTML_MODE_COMPACT));
        } else {
            text.setText(Html.fromHtml(show));
        }
        text.setMovementMethod(LinkMovementMethod.getInstance());
        if (!checkRequired) {
            checkbox.setVisibility(View.GONE);
            buttonDone.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.button_done)
    public void done() {
        if (checkbox.isChecked()) {
            setResult(RESULT_OK);
            finish();
        }
    }

}
