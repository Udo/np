<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title><?= htmlspecialchars($GLOBALS['title']) ?> | np</title>
        <meta name="description" content="">
        <meta name="viewport" content="width=1024">
        <base href="http://np-lang.org/">
        <link rel="stylesheet" href="/css/">
        <link rel="icon" type="image/png" href="favicon.png">
        
        <script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js?autoload=true&lang=lua"></script>
        <script src="/js/"></script>
    </head>
    <body>
        <!--[if lt IE 7]>
            <p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to improve your experience.</p>
        <![endif]-->
        <div class="header-container">
            <header class="">
                <nav>
                    <ul>
                        <li><a href="/">About</a></li>
                        <!--<li><a href="/tutorial">Tutorial</a></li>-->
                        <li><a href="/tutorial">Tutorial</a></li>
                        <!--<li><a href="/docs">Docs</a></li>-->
                        <li><a href="https://github.com/Udo/np">Get</a></li>
                        <li><a href="https://github.com/Udo/np/issues">Bugs</a></li>
                    </ul>
                </nav>
                <h1 class="title"><a href="/">np</a></h1>
            </header>
        </div>
        

        <div class="main-container">
            <div class="main wrapper clearfix">

        <?= $GLOBALS['aside'] ?>

                <article>
                    <header>
                        <h1><?= htmlspecialchars($GLOBALS['title']) ?></h1>
                    </header>
                        
                    <p><?= $content ?></p>
                        
                </article>
            </div> <!-- #main -->
        </div> <!-- #main-container -->

        <div class="footer-container">
            <footer class="wrapper">
                &copy; <?= date('Y') ?> by <a href="mailto:udo.schroeter@gmail.com">Udo Schroeter</a>
                &middot;
                <a href="/donate">Donate</a>
                &middot;
                <a target="_blank" href="http://udos.name/">Hire</a>
            </footer>
        </div>


    </body>
</html>
