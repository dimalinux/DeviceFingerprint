/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
using System;
using System.Collections.Generic;
using System.IO;
using System.IO.IsolatedStorage;
using System.Linq;
using System.Net;
using System.Net.NetworkInformation;
using System.Reflection;
using System.Windows;
using System.Windows.Browser;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;

namespace DeviceInfo
{

    public partial class MainPage : UserControl
    {
        const string COOKIE_NAME = "device";

        [ScriptableMemberAttribute]
        public string OsVersion { get; private set; }

        [ScriptableMemberAttribute]
        public string ClrVersion { get; private set; }

        [ScriptableMemberAttribute]
        public string AssemblyClrVersion { get; private set; }

        [ScriptableMemberAttribute]
        public int SysUptimeMs { get; private set; }

        [ScriptableMemberAttribute]
        public int ProcessorCount { get; private set; }

        [ScriptableMemberAttribute]
        public bool IsolatedStorageEnabled { get; private set; }

        [ScriptableMemberAttribute]
        public bool IsolatedStorageTest { get; private set; }

        [ScriptableMember]
        public string getCookie()
        {
            return getCookie(COOKIE_NAME);
        }

        [ScriptableMember]
        public bool setCookie(string value)
        {
            return setCookie(COOKIE_NAME, value);
        }

        public MainPage()
        {
            InitializeComponent();

            OsVersion = Environment.OSVersion.Version.ToString();
            ClrVersion = Environment.Version.ToString();
            AssemblyClrVersion = Assembly.GetExecutingAssembly().ImageRuntimeVersion;
            SysUptimeMs = Environment.TickCount;
            ProcessorCount = Environment.ProcessorCount;
            IsolatedStorageEnabled = IsolatedStorageFile.IsEnabled;
            IsolatedStorageTest = IsolatedStorageEnabled ? performIsolatedStorageTest() : false;

            HtmlPage.RegisterScriptableObject("DeviceInfo", this);
        }


        private static bool setCookie(string key, string value)
        {
            var setOk = true;

            try
            {
                using (IsolatedStorageFile isf = IsolatedStorageFile.GetUserStoreForApplication())
                {
                    using (IsolatedStorageFileStream isfs = new IsolatedStorageFileStream(key, FileMode.Create, isf))
                    {
                        using (StreamWriter sw = new StreamWriter(isfs))
                        {
                            sw.Write(value);
                            sw.Close();
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                setOk = false;
                log("Silverlight erorr in setCookie(" + key + "," + value + "): " + ex.Message);
            }

            return setOk;
        }


        private static string getCookie(string key)
        {
            string value = null;
            try
            {
                using (IsolatedStorageFile isf = IsolatedStorageFile.GetUserStoreForApplication())
                {
                    if (isf.FileExists(key))
                    {
                        using (IsolatedStorageFileStream isfs = new IsolatedStorageFileStream(key, FileMode.Open, isf))
                        {
                            using (StreamReader sr = new StreamReader(isfs))
                            {
                                value = sr.ReadToEnd();
                                sr.Close();
                            }
                        }
                    }
                }

            }
            catch (Exception ex)
            {
                log("Silverlight error in getCookie(" + key + "): " + ex.Message);
            }

            if (value != null && value.Length == 0)
                value = null;

            return value;
        }

        private static bool performIsolatedStorageTest()
        {
            var didPass = false;

            var testKey = "storage_rw_test";
            var testValue = "abcd1234";

            if (setCookie(testKey, testValue))
            {
                var readValue = getCookie(testKey);
                didPass = testValue.Equals(readValue);
            }

            return didPass;
        }

        private static void log(string message)
        {
            HtmlWindow window = HtmlPage.Window;
            var isConsoleAvailable = (bool)window.Eval("typeof(console) != 'undefined' && typeof(console.log) != 'undefined'");
            if (isConsoleAvailable)
            {
                var consoleLog = (window.Eval("console.log") as ScriptObject);
                if (consoleLog != null)
                {
                    consoleLog.InvokeSelf(message);
                }
            }
        }

    }
}
