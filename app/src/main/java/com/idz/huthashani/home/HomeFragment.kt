package com.idz.huthashani.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.idz.huthashani.R


class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WishCardAdapter

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)



        val data = generateDummyData()
        val imageResources = generateImageList()
        val texts = generateTexts()

        val onCardClick: (position: Int) -> Unit = { position ->
            val dialog = CustomDialogFragment()

            val args = Bundle().apply {
                putString("dialog_text", texts[position])
            }

            dialog.arguments = args
            dialog.show(parentFragmentManager, "CustomDialog") // Open the dialog
        }

        rootView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                // If touched outside the main content area, close the fragment
                parentFragmentManager.popBackStack() // Closes the fragment
                true
            } else {
                false
            }
        }

        // Initialize RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = WishCardAdapter(data, imageResources, onCardClick)
        recyclerView.adapter = adapter

        return rootView
    }



    // Generate dummy data for testing
    private fun generateDummyData(): List<String> {
        val data = mutableListOf<String>()
        for (i in 1..10) {
            data.add("Item $i")
        }
        return data
    }

    private fun generateImageList(): List<Int> {
        return listOf(
            R.drawable.hashachar,
            R.drawable.asheryazar,
            R.drawable.shehakol,
            R.drawable.hadama,
            R.drawable.tree,
            R.drawable.gefen,
            R.drawable.hamoti,
            R.drawable.mezonot,
            R.drawable.halevana,
            R.drawable.hatora,

            )
    }


    private fun generateTexts(): List<String> {
        // Texts to display for each card
        return listOf(
            "מודֶה / מודָה / אֲנִי לְפָנֶיךָ מֶלֶךְ חַי וְקַיָּם, שֶׁהֶחֱזַרְתָּ בִּי נִשְׁמָתִי בְּחֶמְלָה, רַבָּה אֱמוּנָתֶךָ.\n" +
                    "\n" +
                    "בָּרוּךְ אַתָּה יהֵוָהֵ, אֱלהֵינוּ מֶלֶךְ הָעולָם, אֲשֶׁר קִדְּשָׁנוּ בְּמִצְותָיו וְצִוָּנוּ עַל נְטִילַת יָדַיִם:\n" +
                    "\n" +
                    "בָּרוּךְ אַתָּה יהֵוָהֵ, אֱלהֵינוּ מֶלֶךְ הָעולָם, אֲשֶׁר יָצַר אֶת הָאָדָם בְּחָכְמָה. וּבָרָא בו נְקָבִים נְקָבִים. חֲלוּלִים חֲלוּלִים. גָּלוּי וְיָדוּעַ לִפְנֵי כִסֵּא כְבודֶךָ שֶׁאִם יִסָּתֵם אֶחָד מֵהֶם או אִם יִפָּתֵחַ אֶחָד מֵהֶם אֵי אֶפְשַׁר לְהִתְקַיֵּם אֲפִלּוּ שָׁעָה אֶחָת. בָּרוּךְ אַתָּה יהֵוָהֵ, רופֵא כָל בָּשָׂר וּמַפְלִיא לַעֲשׂות:\n" +
                    "\n" +
                    "אֱלהַי. נְשָׁמָה שֶׁנָּתַתָּ בִּי טְהורָה. אַתָּה בְרָאתָהּ. אַתָּה יְצַרְתָּהּ. אַתָּה נְפַחְתָּהּ בִּי. וְאַתָּה מְשַׁמְּרָהּ בְּקִרְבִּי. וְאַתָּה עָתִיד לִטְּלָהּ מִמֶּנִּי וּלְהַחֲזִירָהּ בִּי לֶעָתִיד לָבא. כָּל זְמַן שֶׁהַנְּשָׁמָה בְקִרְבִּי מודֶה אֲנִי לְפָנֶיךָ יהֵוָהֵ אֱלהַי וֵאלהֵי אֲבותַי רִבּון כָּל הַמַּעֲשִׂים. אֲדון כָּל הַנְּשָׁמות. בָּרוּךְ אַתָּה יהֵוָהֵ, הַמַּחֲזִיר נְשָׁמות לִפְגָרִים מֵתִים:\n" +
                    "\n" +
                    "בָּרוּךְ אַתָּה יהֵוָהֵ, אֱלהֵינוּ מֶלֶךְ הָעולָם, הַנּותֵן לַשֶּׂכְוִי בִּינָה לְהַבְחִין בֵּין יום וּבֵין לָיְלָה:\n" +
                    "בָּרוּךְ אַתָּה יהֵוָהֵ, אֱלהֵינוּ מֶלֶךְ הָעולָם, פּוקֵחַ עִוְרִים:\n" +
                    "בָּרוּךְ אַתָּה יהֵוָהֵ, אֱלהֵינוּ מֶלֶךְ הָעולָם, מַתִּיר אֲסוּרִים:\n" +
                    "בָּרוּךְ אַתָּה יהֵוָהֵ, אֱלהֵינוּ מֶלֶךְ הָעולָם, זוקֵף כְּפוּפִים:\n" +
                    "בָּרוּךְ אַתָּה יהֵוָהֵ, אֱלהֵינוּ מֶלֶךְ הָעולָם, מַלְבִּישׁ עֲרֻמִּים:\n" +
                    "בָּרוּךְ אַתָּה יהֵוָהֵ, אֱלהֵינוּ מֶלֶךְ הָעולָם, הַנּותֵן לַיָּעֵף כּחַ:\n" +
                    "בָּרוּךְ אַתָּה יהֵוָהֵ, אֱלהֵינוּ מֶלֶךְ הָעולָם, רוקַע הָאָרֶץ עַל הַמָּיִם:\n" +
                    "בָּרוּךְ אַתָּה יהֵוָהֵ, אֱלהֵינוּ מֶלֶךְ הָעולָם, הַמֵּכִין מִצְעֲדֵי גָבֶר:\n" +
                    "ברוך אַתָּה יהֵוָהֵ, אֱלהֵינוּ מֶלֶךְ הָעולָם, שֶׁעָשָׂה לִי כָּל צָרְכִּי:\n" +
                    "בָּרוּךְ אַתָּה יהֵוָהֵ, אֱלהֵינוּ מֶלֶךְ הָעולָם, אוזֵר יִשְׂרָאֵל בִּגְבוּרָה:\n" +
                    "בָּרוּךְ אַתָּה יהֵוָהֵ, אֱלהֵינוּ מֶלֶךְ הָעולָם, עוטֵר יִשְׂרָאֵל בְּתִפְאָרָה:\n" +
                    "בָּרוּךְ אַתָּה יהֵוָהֵ, אֱלהֵינוּ מֶלֶךְ הָעולָם, שֶׁלּא עָשַׂנִי גוי. (האשה אומרת – גויָה):\n" +
                    "בָּרוּךְ אַתָּה יהֵוָהֵ, אֱלהֵינוּ מֶלֶךְ הָעולָם, שֶׁלּא עָשַׂנִי עָבֶד. (האשה אומרת – שִׁפְחָה):\n" +
                    "בָּרוּךְ אַתָּה יהֵוָהֵ, אֱלהֵינוּ מֶלֶךְ הָעולָם, שֶׁלּא עָשַׂנִי אִשָּׁה:\n" +
                    "(אשה אומרת בלי שם ומלכות – בָּרוּךְ שֶׁעָשַׂנִי כִּרְצונו):\n" +
                    "בָּרוּךְ אַתָּה יהֵוָהֵ, אֱלהֵינוּ מֶלֶךְ הָעולָם, הַמַּעֲבִיר חֶבְלֵי שֵׁנָה מֵעֵינַי וּתְנוּמָה מֵעַפְעַפָּי:\n" +
                    "\n" +
                    "וִיהִי רָצון מִלְּפָנֶיךָ יהֵוָהֵ אֱלהַי וֵאלהֵי אֲבותַי, שֶׁתַּרְגִּילֵנִי בְּתורָתֶךָ. וְתַדְבִּיקֵנִי בְּמִצְותֶיךָ. וְאַל תְּבִיאֵנִי לִידֵי חֵטְא. וְלא לִידֵי עָון. וְלא לִידֵי נִסָּיון. וְלא לִידֵי בִזָּיון. וְתַרְחִיקֵנִי מִיֵּצֶר הָרָע. וְתַדְבִּיקֵנִי בְּיֵצֶר הַטּוב. וְכוף אֶת יִצְרִי לְהִשְׁתַּעְבֶּד לָךְ. וּתְנֵנִי הַיּום וּבְכָל יום לְחֵן וּלְחֶסֶד וּלְרַחֲמִים בְּעֵינֶיךָ וּבְעֵינֵי כָל רואַי. וְגָמְלֵנִי חֲסָדִים טובִים. בָּרוּךְ אַתָּה יהֵוָהֵ, גּומֵל חֲסָדִים טובִים לְעַמּו יִשְׂרָאֵל:\n" +
                    "\n" +
                    "יְהִי רָצון מִלְּפָנֶיךָ יהֵוָהֵ אֱלהַי וֵאלהֵי אֲבותַי שֶׁתַּצִּילֵנִי הַיּום וּבְכָל יום וָיום מֵעַזֵּי פָנִים. וּמֵעַזּוּת פָּנִים. מֵאָדָם רָע. מִיֵּצֶר רָע. מֵחָבֵר רָע. מִשָּׁכֵן רָע. מִפֶּגַע רָע. מֵעַיִן הָרָע. וּמִלָּשׁון הָרָע. מִדִּין קָשֶׁה. וּמִבַּעַל דִּין קָשֶׁה. בֵּין שֶׁהוּא בֶן בְּרִית. וּבֵין שֶׁאֵינו בֶן בְּרִית:\n" +
                    "\n" +
                    "בָּרוּךְ אַתָּה יהֵוָהֵ, אֱלהֵינוּ מֶלֶךְ הָעולָם, אֲשֶׁר קִדְּשָׁנוּ בְּמִצְותָיו וְצִוָּנוּ עַל דִּבְרֵי תורָה:\n" +
                    "וְהַעֲרֵב נָא יהֵוָהֵ אֱלהֵינוּ אֶת דִּבְרֵי תורָתְךָ בְּפִינוּ וּבְפִיפִיּות עַמְּךָ בֵּית יִשְׂרָאֵל. וְנִהְיֶה אֲנַחְנוּ וְצֶאֱצָאֵינוּ וְצֶאֱצָאֵי צֶאֱצָאֵינוּ כֻּלָּנוּ יודְעֵי שְׁמֶךָ וְלומְדֵי תורָתְךָ לִשְׁמָהּ. בָּרוּךְ אַתָּה יהֵוָהֵ, הַמְלַמֵּד תּורָה לְעַמּו יִשְׂרָאֵל:\n" +
                    "\n" +
                    "בָּרוּךְ אַתָּה יהֵוָהֵ, אֱלהֵינוּ מֶלֶךְ הָעולָם, אֲשֶׁר בָּחַר בָּנוּ מִכָּל הָעַמִּים וְנָתַן לָנוּ אֶת תּורָתו. בָּרוּךְ אַתָּה יהֵוָהֵ, נותֵן הַתּורָה:\n" +
                    "\n" +
                    "וַיְדַבֵּר יהֵוָהֵ אֶל משֶׁה לֵּאמר:\n" +
                    "דַּבֵּר אֶל אַהֲרן וְאֶל בָּנָיו לֵאמר כּה תְבָרְכוּ אֶת בְּנֵי יִשְׂרָאֵל. אָמור לָהֶם:\n" +
                    "\n" +
                    "יְבָרֶכְךָ יהֵוָהֵ וְיִשְׁמְרֶךָ:\n" +
                    "יָאֵר יהֵוָהֵ פָּנָיו אֵלֶיךָ וִיחֻנֶּךָּ:\n" +
                    "יִשָּׂא יהֵוָהֵ  פָּנָיו אֵלֶיךָ וְיָשֵׂם לְךָ שָׁלום:\n" +
                    "וְשָׂמוּ אֶת שְׁמִי עַל בְּנֵי יִשְׂרָאֵל וַאֲנִי אֲבָרְכֵם:",


            "בָּרוּךְ אַתָּה יהֵוָהֵ, אֱלהֵינוּ מֶלֶךְ הָעולָם, אֲשֶׁר יָצַר אֶת הָאָדָם בְּחָכְמָה. וּבָרָא בו נְקָבִים נְקָבִים. חֲלוּלִים חֲלוּלִים. גָּלוּי וְיָדוּעַ לִפְנֵי כִסֵּא כְבודֶךָ שֶׁאִם יִסָּתֵם אֶחָד מֵהֶם או אִם יִפָּתֵחַ אֶחָד מֵהֶם אֵי אֶפְשַׁר לְהִתְקַיֵּם אֲפִלּוּ שָׁעָה אֶחָת. בָּרוּךְ אַתָּה יהֵוָהֵ, רופֵא כָל בָּשָׂר וּמַפְלִיא לַעֲשׂות:",

            "בָּרוּךְ אַתָּה יהֵוָהֵ אֱלהֵינוּ מֶלֶךְ הָעולָם שֶׁהַכֹּל ניהיה בִּדְבָרו.\n" +
                    "\n" +
                    "מהי ברכת שהכל\n" +
                    "ברכת שהכל, הינה אחת משבעת ברכות הנהנין, אשר נקבעה לאמרה קודם כל דבר שאינו מן הקרקע.\n" +
                    "\n" +
                    "באוכל: את ברכת שהכל ניהיה בדברו מברכים על כל דבר שאין גידולו מן הקרקע (פירות וירקות), וכן על דבר שאינו עשוי מקמח.\n" +
                    "\n" +
                    "במשקאות: מברכים שהכל על כל המשקאות למעט יין, שעליו מברכים ברכת הגפן.\n" +
                    "\n" +
                    "בפירות וירקות: כאשר השתנו צורתם של הירקות או הפירות, כגון שמעכו היטב עד שלא ניתן להכיר את הירק או הפרי – הרי זה מברך עליו “שהכל”.\n" +
                    "וכן, קליפות ירקות ופירות שאין דרכם לאוכלם, למרות שהמתיקו הרי זה מברך עליהם “שהכל”.\n" +
                    "ירק או פרי מבושל שדרכו לאוכלו “חי”, הרי זה מברך עליו “שהכל”.\n" +
                    "\n" +
                    "אוכל מן החי: כגון בשר, ביצים ודגים, הרי זה מברך עליהם \"שהכל\".",

            "בָּרוּךְ אַתָּה יהֵוָהֵ, אֱלהֵינוּ מֶלֶךְ הָעולָם בּוֹרֵא פְּרִי הָאֲדָמָה.\n" +
                    "\n" +
                    "מהי ברכת האדמה\n" +
                    "ברכת האדמה, הינה אחת משבעת ברכות הנהנין, אשר נקבעה לאמרה קודם אכילת אחת או יותר מפירות האדמה (ירקות). \n" +
                    "\n" +
                    "את ברכת בורא פרי האדמה מברכים על כל דבר שגידולו מן הקרקע כגון ירקות, זרעים וצמחים שאינם גדלים על העץ.\n" +
                    "\n" +
                    "עם זאת בננה, למרות היותה גדלה על העץ, הרי מברכים עליה \"בורא פרי האדמה\".\n" +
                    "\n" +
                    "כאשר השתנתה צורתו של הירק, כגון שמעכו עד כדי שלא ניתן להכיר את הירק – הרי זה מברך עליו“שהכל”.\n" +
                    "\n" +
                    "קליפות ירקות שאין דרכם לאוכלם, למרות שהמתיקו הרי זה מברך עליהם “שהכל”.\n" +
                    "\n" +
                    "ירק מבושל שדרכו לאוכלו “חי”, הרי זה מברך עליו “שהכל”.",

            "בָּרוּךְ אַתָּה יהֵוָהֵ אֱלהֵינוּ מֶלֶךְ הָעולָם בּוֹרֵא פְּרִי הָעֵץ\n" +
                    "\n" +
                    "מהי ברכת העץ\n" +
                    "ברכת העץ, הינה אחת משבעת ברכות הנהנין, אשר נקבעה לאמרה קודם אכילת אחת או יותר מפירות העץ. \n" +
                    "\n" +
                    "את ברכת בורא פרי העץ מברכים על כל דבר שגידולו מן העץ למעט ענבים שעליו מברכים: \"על הגפן\".\n" +
                    "כמו כן, על בננה, למרות היותה גודלת על העץ, מברכים עליה \"בורא פרי האדמה\".\n" +
                    "כאשר השתנתה צורתו של הפרי, כגון שמעכו עד כדי שלא ניתן להכיר את הפרי – מברך \"שהכל\".\n" +
                    "\n" +
                    "קליפות פירות שאין דרכם לאוכלם כגון קליפות התפוז וכדומה, הרי זה מברך עליהם \"שהכל\". \n" +
                    "פרי מבושל שדרכו לאוכלו כאשר הינו \"חי\", הרי זה מברך עליו \"שהכל\".",

            "בָּרוּךְ אַתָּה יהֵוָהֵ אֱלהֵינוּ מֶלֶךְ הָעולָם בּוֹרֵא פְּרִי הַגָּפֶן.\n" +
                    "\n" +
                    "מהי ברכת הגפן\n" +
                    "ברכת הגפן, הינה אחת משבעת ברכות הנהנין, אשר נקבעה לאמרה קודם שתיית יין או כל המשקה העשוי מן הענבים.\n" +
                    "\n" +
                    "את ברכת בורא פרי הגפן מברכים על שתיית כל סוגי היין (הנעשו מן הענבים). עם זאת על ענבים עצמם, מברך עליהם \"בורא פרי העץ\".",

            "בָּרוּךְ אַתָּה יהֵוָהֵ אֱלהֵינוּ מֶלֶךְ הָעולָם הַמּוֹצִיא לֶחֶם מִן הָאָרֶץ.\n" +
                    "\n" +
                    "מהי ברכת המוציא\n" +
                    "ברכת המוציא, הינה אחת מברכות הנהנין, הנאמרת קודם אכילת פת.\n" +
                    "\n" +
                    "את ברכת המוציא, תיקנו חכמים לברכו אם אוכל כל סוג פת העשוי מחיטה או שעורה. ולמה תיקנו זאת? משום חשיבותו הגדולה של הלחם, הידוע כמשביעה כפי שנאמר בתהילים פרק קד' פסוק טו' \"וְ֝לֶ֗חֶם לְֽבַב אֱנ֥וֹשׁ יִסְעָֽד\".\n" +
                    "\n" +
                    "כמו כן, גם אם אוכל פת שאין בו שיעור כזית, אפילו פירורי לחם, הרי זה חייב בברכת המוציא לחם מן הארץ.",

            "בָּרוּךְ אַתָּה יהֵוָהֵ אֱלהֵינוּ מֶלֶךְ הָעולָם בּוֹרֵא מִינֵי מְזוֹנוֹת.\n" +
                    "\n" +
                    "מהם ברכות המצוות?\n" +
                    "ברכת מזונות הינה אחת מברכות הנהנין, אשר תיקנו לאומרה קודם אכילת מזונות (דבר מזין) שאינו פת.\n" +
                    "\n" +
                    "את ברכת מזונות מברכים אם אכל אחת או יותר מחמשת מיני דגן (חיטה, שעורה, כוסמין, שיפון, שיבולת שועל), ואשר על אלו לא קובעים סעודה (כגון לחם וכדומה).\n" +
                    "\n" +
                    "עם זאת ישנם גרסאות הסוברות שגם בחלק מאכילת מזונות יש לברך המוציא, אם שיעור האכילה עולה על 250 גרם, או שקבעו על אכילה זו סעודה.",

            "לַמְנַצֵּחַ מִזְמוֹר לְדָוִד: הַשָּׁמַיִם מְסַפְּרִים כְּבוֹד אֵל וּמַעֲשֵׂה יָדָיו מַגִּיד הָרָקִיעַ: יוֹם לְיוֹם יַבִּיעַ אֹמֶר וְלַיְלָה לְּלַיְלָה יְחַוֶּה דָּעַת: אֵין אֹמֶר וְאֵין דְּבָרִים בְּלִי נִשְׁמָע קוֹלָם: בְּכָל הָאָרֶץ יָצָא קַוָּם וּבִקְצֵה תֵבֵל מִלֵּיהֶם לַשֶּׁמֶשׁ שָׂם אֹהֶל בָּהֶם: וְהוּא כְּחָתָן יֹצֵא מֵחֻפָּתוֹ יָשִׂישׂ כְּגִבּוֹר לָרוּץ אֹרַח: מִקְצֵה הַשָּׁמַיִם מוֹצָאוֹ וּתְקוּפָתוֹ עַל קְצוֹתָם וְאֵין נִסְתָּר מֵחַמָּתוֹ: תּוֹרַת יְהֹוָה תְּמִימָה מְשִׁיבַת נָפֶשׁ עֵדוּת יְהֹוָה נֶאֱמָנָה מַחְכִּימַת פֶּתִי: פִּקּוּדֵי יְהֹוָה יְשָׁרִים מְשַׂמְּחֵי לֵב מִצְוַת יְהֹוָה בָּרָה מְאִירַת עֵינָיִם: יִרְאַת יְהֹוָה טְהוֹרָה עוֹמֶדֶת לָעַד מִשְׁפְּטֵי יְהֹוָה אֱמֶת צָדְקוּ יַחְדָּו: הַנֶּחֱמָדִים מִזָּהָב וּמִפַּז רָב וּמְתוּקִים מִדְּבַשׁ וְנֹפֶת צוּפִים: גַּם עַבְדְּךָ נִזְהָר בָּהֶם בְּשָׁמְרָם עֵקֶב רָב: שְׁגִיאוֹת מִי יָבִין מִנִּסְתָּרוֹת נַקֵּנִי: גַּם מִזֵּדִים חֲשֹׂךְ עַבְדֶּךָ אַל יִמְשְׁלוּ בִי אָז אֵיתָם וְנִקֵּיתִי מִפֶּשַׁע רָב: יִהְיוּ לְרָצוֹן אִמְרֵי פִי וְהֶגְיוֹן לִבִּי לְפָנֶיךָ יְהֹוָה צוּרִי וְגֹאֲלִי:\n" +
                    "\n" +
                    "צוּרִי בָּעוֹלָם הַזֶּה וגוֹאֲלִי לָעוֹלָם הַבָּא. וְכָל קַרְנֵי רְשָׁעִים אֲגַדֵּעַ תְּרוֹמַמְנָה קַרְנוֹת צַדִּיק:\n" +
                    "\n" +
                    "הַלְלוּיָהּ הַלְלוּ אֶת יְהֹוָה מִן הַשָּׁמַיִם הַלְלוּהוּ בַּמְּרוֹמִים: הַלְלוּהוּ כָל מַלְאָכָיו הַלְלוּהוּ כָּל צְבָאָיו: הַלְלוּהוּ שֶׁמֶשׁ וְיָרֵחַ הַלְלוּהוּ כָּל כּוֹכְבֵי אוֹר: הַלְלוּהוּ שְׁמֵי הַשָּׁמָיִם וְהַמַּיִם אֲשֶׁר מֵעַל הַשָּׁמָיִם: יְהַלְלוּ אֶת שֵׁם יְהֹוָה כִּי הוּא צִוָּה וְנִבְרָאוּ: וַיַּעֲמִידֵם לָעַד לְעוֹלָם חָק נָתַן וְלֹא יַעֲבוֹר:\n" +
                    "\n" +
                    "(כשאומר 'כי אראה שמיך' יסתכל על הלבנה)\n" +
                    "\n" +
                    "כִּי אֶרְאֶה שָׁמֶיךָ מַעֲשֵׂה אֶצְבְּעוֹתֶיךָ יָרֵח וְכוֹכָבִים אֲשֶׁר כּוֹנָנְתָּ: יְהֹוָה אֲדֹנֵינוּ, מָה אַדִּיר שִׁמְךָ בְּכָל הָאָרֶץ:\n" +
                    "\n" +
                    "לְשֵׁם יִחוּד קֻדְשָׁא בְּרִיךְ הוּא וּשְכִינְתֵּהּ, בִּדְחִילוּ וּרְחִימוּ, וּרְחִימוּ וּדְחִילוּ, ליַחֲדָא שֵׁם אוֹת יוּ\"ד אוֹת הֵ\"א בּאוֹת וָא\"ו אוֹת הֵ\"א, בּיִחוּדָא שְׁלִים בּשֵׁם כָּל יִשְׂרָאֵל. הִנֵּה אֲנַחְנוּ בָּאִים לְבָרֵךְ בִּרְכַּת הַלְּבָנָה כְּמוֹ שֶׁתִּקְּנוּ לָנוּ רַבּוֹתֵינוּ זִכְרוֹנָם לִבְרָכָה, עִם כָּל הַמִּצְוֹת הַכְּלוּלוֹת בָּהּ, לְתַקֵּן אֶת שָׁרְשָׁהּ בְּמָקוֹם עֶלְיוֹן, לַעֲשׂוֹת נַחַת רוּחַ לְיוֹצְרֵנוּ וְלַעֲשׂוֹת רְצוֹן בּוֹרְאֵנוּ, וִיהִי נֹעַם אֲדֹנָי אֱלֹהֵינוּ עָלֵינוּ, וּמַעֲשֵׂה יָדֵינוּ כּוֹננָה עָלֵינוּ, וּמַעֲשֵׂה יָדֵינוּ כּוֹנְנֵהוּ: וּמַעֲשֵׂה יָדֵינוּ כּוֹנְנֵהוּ:\n" +
                    "\n" +
                    "בָּרוּךְ אַתָּה יְהֹוָה, אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם, אֲשֶׁר בְּמַאֲמָרוֹ בָּרָא שְׁחָקִים וּבְרוּחַ פִּיו כָּל צְבָאָם, חֹק וּזְמַן נָתַן לָהֶם שֶׁלֹּא יְשַׁנוּ אֶת תַּפְקִידָם, שָׂשִׂים וּשְׂמֵחִים לַעֲשׂוֹת רְצוֹן קוֹנֵיהֶם, פּוֹעֵל אֱמֶת שֶׁפְּעֻלָּתוֹ אֱמֶת, וְלַלְּבָנָה אָמַר שֶׁתִּתְחַדֵּשׁ, עֲטֶרֶת תִּפְאֶרֶת לַעֲמוּסֵי בָטֶן, שֶׁגַּם הֵם עֲתִידִים לְהִתְחַדֵּשׁ כְּמוֹתָהּ וּלְפָאֵר לְיוֹצְרָם עַל שֵׁם כְּבוֹד מַלְכוּתוֹ. בָּרוּךְ אַתָּה יְהֹוָה, מְחַדֵּשׁ חֳדָשִׁים:\n" +
                    "\n" +
                    "סִימָן טוֹב תְּהִי לָנוּ וּלְכָל יִשְׂרָאֵל:\n" +
                    "סִימָן טוֹב תְּהִי לָנוּ וּלְכָל יִשְׂרָאֵל:\n" +
                    "סִימָן טוֹב תְּהִי לָנוּ וּלְכָל יִשְׂרָאֵל:\n" +
                    "\n" +
                    "יאמר ג' פעמים – בָּרוּךְ יוֹצְרֵךְ, בָּרוּךְ עוֹשֵׂךְ, בָּרוּךְ קוֹנֵךְ, בָּרוּךְ בּוֹראֵךְ, כְּשֵׁם שֶׁאֲנַחְנוּ מרַקְּדִים (ידלג ג\"פ) כְּנֶגְדֵּךְ, וְאֵין אֲנַחְנוּ יְכוֹלִים לִגַּע בָּךְ, כָּךְ אִם יְרַקְּדוּ אֲחֵרִים כְּנֶגְדֵּנוּ לְהַזִּיקֵנוּ, לֹא יוּכְלוּ לִגַּע בָּנוּ, וְלֹא יִשְׁלְטוּ בָנוּ, וְלֹא יַעֲשׂוּ בָנוּ שׁוּם רֹשֶׁם כְּלָל ועִקָר.\n" +
                    "\n" +
                    "תִּפֹּל עֲלֵיהֶם אֵימָתָה וָפַחַד, בִּגְדֹל זְרוֹעֲךָ יִדְּמוּ כָּאָבֶן: כָּאָבֶן יִדְּמוּ, זְרוֹעֲךָ בִּגְדֹל, וָפַחַד אֵימָתָה עֲלֵיהֶם תִּפֹּל:\n" +
                    "\n" +
                    "דָּוִד מֶלֶךְ יִשְׂרָאֵל חַי וקַיָּם: דָּוִד מֶלֶךְ יִשְׂרָאֵל חַי וקַיָּם: דָּוִד מֶלֶךְ יִשְׂרָאֵל חַי וקַיָּם: \n" +
                    "\n" +
                    "אָמֵן, אָמֵן, אָמֵן: נֶצַח, נֶצַח, נֶצַח: סֶלָה, סֶלָה, סֶלָה: וָעֶד, וָעֶד, וָעֶד:\n" +
                    "\n" +
                    "לֵב טָהוֹר בְּרָא לִי אֱלֹהִים, וְרוּחַ נָכוֹן חַדֵּשׁ בְּקִרְבִּי:\n" +
                    "לֵב טָהוֹר בְּרָא לִי אֱלֹהִים, וְרוּחַ נָכוֹן חַדֵּשׁ בְּקִרְבִּי:\n" +
                    "לֵב טָהוֹר בְּרָא לִי אֱלֹהִים, וְרוּחַ נָכוֹן חַדֵּשׁ בְּקִרְבִּי:\n" +
                    "לֵב טָהוֹר בְּרָא לִי אֱלֹהִים, וְרוּחַ נָכוֹן חַדֵּשׁ בְּקִרְבִּי:\n" +
                    "לֵב טָהוֹר בְּרָא לִי אֱלֹהִים, וְרוּחַ נָכוֹן חַדֵּשׁ בְּקִרְבִּי:\n" +
                    "לֵב טָהוֹר בְּרָא לִי אֱלֹהִים, וְרוּחַ נָכוֹן חַדֵּשׁ בְּקִרְבִּי:\n" +
                    "לֵב טָהוֹר בְּרָא לִי אֱלֹהִים, וְרוּחַ נָכוֹן חַדֵּשׁ בְּקִרְבִּי:\n" +
                    "שִׁיר לַמַּעֲלוֹת אֶשָּׂא עֵינַי אֶל הֶהָרִים מֵאַיִן יָבֹא עֶזְרִי: עֶזְרִי מֵעִם יְהֹוָה עֹשֵׂה שָׁמַיִם וָאָרֶץ: אַל יִתֵּן לַמּוֹט רַגְלֶךָ אַל יָנוּם שֹׁמְרֶךָ: הִנֵּה לֹא יָנוּם וְלֹא יִישָׁן שׁוֹמֵר יִשְׂרָאֵל: יְהֹוָה שֹׁמְרֶךָ יְהֹוָה צִלְּךָ עַל יַד יְמִינֶךָ: יוֹמָם הַשֶּׁמֶשׁ לֹא יַכֶּכָּה וְיָרֵחַ בַּלָּיְלָה: יְהֹוָה יִשְׁמָרְךָ מִכָּל רָע יִשְׁמֹר אֶת נַפְשֶׁךָ: יְהֹוָה יִשְׁמָר צֵאתְךָ וּבוֹאֶךָ מֵעַתָּה וְעַד עוֹלָם:\n" +
                    "\n" +
                    "הַלְלוּיָהּ הַלְלוּ אֵל בְּקָדְשׁוֹ הַלְלוּהוּ בִּרְקִיעַ עֻזּוֹ: הַלְלוּהוּ בִגְבוּרֹתָיו הַלְלוּהוּ כְּרֹב גֻּדְלוֹ: הַלְלוּהוּ בְּתֵקַע שׁוֹפָר הַלְלוּהוּ בְּנֵבֶל וְכִנּוֹר: הַלְלוּהוּ בְתֹף וּמָחוֹל הַלְלוּהוּ בְּמִנִּים וְעוּגָב: הַלְלוּהוּ בְצִלְצְלֵי שָׁמַע הַלְלוּהוּ בְּצִלְצְלֵי תְרוּעָה: כָּל הַנְּשָׁמָה תְּהַלֵּל יָהּ הַלְלוּיָהּ:\n" +
                    "\n" +
                    "תָּנָא דְבֵי רִבִּי יִשְׁמָעֵאל אִלְמָלֵי לֹא זָכוּ בְנֵי יִשְׂרָאֵל אֶלָּא לְהַקְבִּיל פְּנֵי אֲבִיהֶם שֶׁבַּשָּׁמַיִם פַּעַם אַחַת בַּחֹדֶשׁ דַּיָּם. אָמַר אַבַּיֵּי הֵלְכָךְ נִימְרִינְהוּ מֵעֹמֶד:",

           "בָּרוּךְ אַתָּה יהֵוָהֵ, אֱלהֵינוּ מֶלֶךְ הָעולָם, אֲשֶׁר קִדְּשָׁנוּ בְּמִצְותָיו וְצִוָּנוּ עַל דִּבְרֵי תורָה:\n" +
                   "וְהַעֲרֵב נָא יהֵוָהֵ אֱלהֵינוּ אֶת דִּבְרֵי תורָתְךָ בְּפִינוּ וּבְפִיפִיּות עַמְּךָ בֵּית יִשְׂרָאֵל. וְנִהְיֶה אֲנַחְנוּ וְצֶאֱצָאֵינוּ וְצֶאֱצָאֵי צֶאֱצָאֵינוּ כֻּלָּנוּ יודְעֵי שְׁמֶךָ וְלומְדֵי תורָתְךָ לִשְׁמָהּ. בָּרוּךְ אַתָּה יהֵוָהֵ, הַמְלַמֵּד תּורָה לְעַמּו יִשְׂרָאֵל:\n" +
                   "\n" +
                   "בָּרוּךְ אַתָּה יהֵוָהֵ, אֱלהֵינוּ מֶלֶךְ הָעולָם, אֲשֶׁר בָּחַר בָּנוּ מִכָּל הָעַמִּים וְנָתַן לָנוּ אֶת תּורָתו. בָּרוּךְ אַתָּה יהֵוָהֵ, נותֵן הַתּורָה:\n" +
                   "\n" +
                   "וַיְדַבֵּר יהֵוָהֵ אֶל משֶׁה לֵּאמר:\n" +
                   "דַּבֵּר אֶל אַהֲרן וְאֶל בָּנָיו לֵאמר כּה תְבָרְכוּ אֶת בְּנֵי יִשְׂרָאֵל. אָמור לָהֶם:\n" +
                   "\n" +
                   "יְבָרֶכְךָ יהֵוָהֵ וְיִשְׁמְרֶךָ:\n" +
                   "יָאֵר יהֵוָהֵ פָּנָיו אֵלֶיךָ וִיחֻנֶּךָּ:\n" +
                   "יִשָּׂא יהֵוָהֵ  פָּנָיו אֵלֶיךָ וְיָשֵׂם לְךָ שָׁלום:\n" +
                   "וְשָׂמוּ אֶת שְׁמִי עַל בְּנֵי יִשְׂרָאֵל וַאֲנִי אֲבָרְכֵם:",

            )
    }
}
