package com.fenda.calendar.model;

import java.util.List;

/**
 * @Author: david.lvfujiang
 * @Date: 2019/10/10
 * @Describe: 查询节日剩余天数
 */
public class QueryLastDayBean {

    /**
     * recordId : 8b63be7517124c05af68fc8a65dd6b94
     * skillId : 2019031800001161
     * requestId : 8b63be7517124c05af68fc8a65dd6b94
     * nlu : {"skillId":"2019031800001161","res":"tasks","input":"春节还有多少时间到","loadtime":13.02685546875,"inittime":21.3701171875,"skill":"日历","skillVersion":"38","source":"aidui","systime":84.23193359375,"semantics":{"request":{"slots":[{"name":"intent","value":"查询天数"},{"pos":[0,1],"rawvalue":"春节","name":"节日节气","rawpinyin":"chun jie","value":"春节"},{"name":"查询对象","value":"剩余天数"}],"task":"日历","confidence":0.88888888888889,"slotcount":3}},"version":"2019.1.15.20:40:58","timestamp":1570692620}
     * dm : {"input":"春节还有多少时间到","shouldEndSession":true,"task":"日历","widget":{"widgetName":"default","duiWidget":"text","extra":{"nlyear":"庚子","before":{"negative":true,"nlyear":"己亥","month":"2","year":"2019","daysInterval":247,"zodiac":"猪","festival":"春节","weekday":"星期二","text":"2月5日","day":"5","nlmonth":"正月","nlday":"初一"},"year":"2020","daysInterval":107,"zodiac":"鼠","festival":"春节","weekday":"星期六","nlmonth":"正月","negative":false,"month":"1","text":"1月25日","day":"25","nlday":"初一"},"name":"default","text":"1月25日","type":"text"},"intentName":"查询天数","runSequence":"nlgFirst","nlg":"2020年春节是1月25日，距今还有107天","intentId":"5d8ae7040d32eb000d70f89d","speak":{"text":"2020年春节是1月25日，距今还有107天","type":"text"},"command":{"api":""},"taskId":"5d8ae7040d32eb000d70f883","status":1}
     * contextId : 7ed2b969d186431ea2af5d6ea5597f60
     * sessionId : 7ed2b969d186431ea2af5d6ea5597f60
     */

    private String recordId;
    private String skillId;
    private String requestId;
    private NluBean nlu;
    private DmBean dm;
    private String contextId;
    private String sessionId;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public NluBean getNlu() {
        return nlu;
    }

    public void setNlu(NluBean nlu) {
        this.nlu = nlu;
    }

    public DmBean getDm() {
        return dm;
    }

    public void setDm(DmBean dm) {
        this.dm = dm;
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public static class NluBean {
        /**
         * skillId : 2019031800001161
         * res : tasks
         * input : 春节还有多少时间到
         * loadtime : 13.02685546875
         * inittime : 21.3701171875
         * skill : 日历
         * skillVersion : 38
         * source : aidui
         * systime : 84.23193359375
         * semantics : {"request":{"slots":[{"name":"intent","value":"查询天数"},{"pos":[0,1],"rawvalue":"春节","name":"节日节气","rawpinyin":"chun jie","value":"春节"},{"name":"查询对象","value":"剩余天数"}],"task":"日历","confidence":0.88888888888889,"slotcount":3}}
         * version : 2019.1.15.20:40:58
         * timestamp : 1570692620
         */

        private String skillId;
        private String res;
        private String input;
        private double loadtime;
        private double inittime;
        private String skill;
        private String skillVersion;
        private String source;
        private double systime;
        private SemanticsBean semantics;
        private String version;
        private int timestamp;

        public String getSkillId() {
            return skillId;
        }

        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }

        public String getRes() {
            return res;
        }

        public void setRes(String res) {
            this.res = res;
        }

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public double getLoadtime() {
            return loadtime;
        }

        public void setLoadtime(double loadtime) {
            this.loadtime = loadtime;
        }

        public double getInittime() {
            return inittime;
        }

        public void setInittime(double inittime) {
            this.inittime = inittime;
        }

        public String getSkill() {
            return skill;
        }

        public void setSkill(String skill) {
            this.skill = skill;
        }

        public String getSkillVersion() {
            return skillVersion;
        }

        public void setSkillVersion(String skillVersion) {
            this.skillVersion = skillVersion;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public double getSystime() {
            return systime;
        }

        public void setSystime(double systime) {
            this.systime = systime;
        }

        public SemanticsBean getSemantics() {
            return semantics;
        }

        public void setSemantics(SemanticsBean semantics) {
            this.semantics = semantics;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public static class SemanticsBean {
            /**
             * request : {"slots":[{"name":"intent","value":"查询天数","pos":[0,1],"rawvalue":"春节","rawpinyin":"chun jie"},{"pos":[0,1],"rawvalue":"春节","name":"节日节气","rawpinyin":"chun jie","value":"春节"},{"name":"查询对象","value":"剩余天数"}],"task":"日历","confidence":0.88888888888889,"slotcount":3}
             */

            private RequestBean request;

            public RequestBean getRequest() {
                return request;
            }

            public void setRequest(RequestBean request) {
                this.request = request;
            }

            public static class RequestBean {
                /**
                 * slots : [{"name":"intent","value":"查询天数"},{"pos":[0,1],"rawvalue":"春节","name":"节日节气","rawpinyin":"chun jie","value":"春节"},{"name":"查询对象","value":"剩余天数"}]
                 * task : 日历
                 * confidence : 0.88888888888889
                 * slotcount : 3
                 */

                private String task;
                private double confidence;
                private int slotcount;
                private List<SlotsBean> slots;

                public String getTask() {
                    return task;
                }

                public void setTask(String task) {
                    this.task = task;
                }

                public double getConfidence() {
                    return confidence;
                }

                public void setConfidence(double confidence) {
                    this.confidence = confidence;
                }

                public int getSlotcount() {
                    return slotcount;
                }

                public void setSlotcount(int slotcount) {
                    this.slotcount = slotcount;
                }

                public List<SlotsBean> getSlots() {
                    return slots;
                }

                public void setSlots(List<SlotsBean> slots) {
                    this.slots = slots;
                }

                public static class SlotsBean {
                    /**
                     * name : intent
                     * value : 查询天数
                     * pos : [0,1]
                     * rawvalue : 春节
                     * rawpinyin : chun jie
                     */

                    private String name;
                    private String value;
                    private String rawvalue;
                    private String rawpinyin;
                    private List<Integer> pos;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getValue() {
                        return value;
                    }

                    public void setValue(String value) {
                        this.value = value;
                    }

                    public String getRawvalue() {
                        return rawvalue;
                    }

                    public void setRawvalue(String rawvalue) {
                        this.rawvalue = rawvalue;
                    }

                    public String getRawpinyin() {
                        return rawpinyin;
                    }

                    public void setRawpinyin(String rawpinyin) {
                        this.rawpinyin = rawpinyin;
                    }

                    public List<Integer> getPos() {
                        return pos;
                    }

                    public void setPos(List<Integer> pos) {
                        this.pos = pos;
                    }
                }
            }
        }
    }

    public static class DmBean {
        /**
         * input : 春节还有多少时间到
         * shouldEndSession : true
         * task : 日历
         * widget : {"widgetName":"default","duiWidget":"text","extra":{"nlyear":"庚子","before":{"negative":true,"nlyear":"己亥","month":"2","year":"2019","daysInterval":247,"zodiac":"猪","festival":"春节","weekday":"星期二","text":"2月5日","day":"5","nlmonth":"正月","nlday":"初一"},"year":"2020","daysInterval":107,"zodiac":"鼠","festival":"春节","weekday":"星期六","nlmonth":"正月","negative":false,"month":"1","text":"1月25日","day":"25","nlday":"初一"},"name":"default","text":"1月25日","type":"text"}
         * intentName : 查询天数
         * runSequence : nlgFirst
         * nlg : 2020年春节是1月25日，距今还有107天
         * intentId : 5d8ae7040d32eb000d70f89d
         * speak : {"text":"2020年春节是1月25日，距今还有107天","type":"text"}
         * command : {"api":""}
         * taskId : 5d8ae7040d32eb000d70f883
         * status : 1
         */

        private String input;
        private boolean shouldEndSession;
        private String task;
        private WidgetBean widget;
        private String intentName;
        private String runSequence;
        private String nlg;
        private String intentId;
        private SpeakBean speak;
        private CommandBean command;
        private String taskId;
        private int status;

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public boolean isShouldEndSession() {
            return shouldEndSession;
        }

        public void setShouldEndSession(boolean shouldEndSession) {
            this.shouldEndSession = shouldEndSession;
        }

        public String getTask() {
            return task;
        }

        public void setTask(String task) {
            this.task = task;
        }

        public WidgetBean getWidget() {
            return widget;
        }

        public void setWidget(WidgetBean widget) {
            this.widget = widget;
        }

        public String getIntentName() {
            return intentName;
        }

        public void setIntentName(String intentName) {
            this.intentName = intentName;
        }

        public String getRunSequence() {
            return runSequence;
        }

        public void setRunSequence(String runSequence) {
            this.runSequence = runSequence;
        }

        public String getNlg() {
            return nlg;
        }

        public void setNlg(String nlg) {
            this.nlg = nlg;
        }

        public String getIntentId() {
            return intentId;
        }

        public void setIntentId(String intentId) {
            this.intentId = intentId;
        }

        public SpeakBean getSpeak() {
            return speak;
        }

        public void setSpeak(SpeakBean speak) {
            this.speak = speak;
        }

        public CommandBean getCommand() {
            return command;
        }

        public void setCommand(CommandBean command) {
            this.command = command;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public static class WidgetBean {
            /**
             * widgetName : default
             * duiWidget : text
             * extra : {"nlyear":"庚子","before":{"negative":true,"nlyear":"己亥","month":"2","year":"2019","daysInterval":247,"zodiac":"猪","festival":"春节","weekday":"星期二","text":"2月5日","day":"5","nlmonth":"正月","nlday":"初一"},"year":"2020","daysInterval":107,"zodiac":"鼠","festival":"春节","weekday":"星期六","nlmonth":"正月","negative":false,"month":"1","text":"1月25日","day":"25","nlday":"初一"}
             * name : default
             * text : 1月25日
             * type : text
             */

            private String widgetName;
            private String duiWidget;
            private ExtraBean extra;
            private String name;
            private String text;
            private String type;

            public String getWidgetName() {
                return widgetName;
            }

            public void setWidgetName(String widgetName) {
                this.widgetName = widgetName;
            }

            public String getDuiWidget() {
                return duiWidget;
            }

            public void setDuiWidget(String duiWidget) {
                this.duiWidget = duiWidget;
            }

            public ExtraBean getExtra() {
                return extra;
            }

            public void setExtra(ExtraBean extra) {
                this.extra = extra;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public static class ExtraBean {
                /**
                 * nlyear : 庚子
                 * before : {"negative":true,"nlyear":"己亥","month":"2","year":"2019","daysInterval":247,"zodiac":"猪","festival":"春节","weekday":"星期二","text":"2月5日","day":"5","nlmonth":"正月","nlday":"初一"}
                 * year : 2020
                 * daysInterval : 107
                 * zodiac : 鼠
                 * festival : 春节
                 * weekday : 星期六
                 * nlmonth : 正月
                 * negative : false
                 * month : 1
                 * text : 1月25日
                 * day : 25
                 * nlday : 初一
                 */

                private String nlyear;
                private BeforeBean before;
                private String year;
                private int daysInterval;
                private String zodiac;
                private String festival;
                private String weekday;
                private String nlmonth;
                private boolean negative;
                private String month;
                private String text;
                private String day;
                private String nlday;

                public String getNlyear() {
                    return nlyear;
                }

                public void setNlyear(String nlyear) {
                    this.nlyear = nlyear;
                }

                public BeforeBean getBefore() {
                    return before;
                }

                public void setBefore(BeforeBean before) {
                    this.before = before;
                }

                public String getYear() {
                    return year;
                }

                public void setYear(String year) {
                    this.year = year;
                }

                public int getDaysInterval() {
                    return daysInterval;
                }

                public void setDaysInterval(int daysInterval) {
                    this.daysInterval = daysInterval;
                }

                public String getZodiac() {
                    return zodiac;
                }

                public void setZodiac(String zodiac) {
                    this.zodiac = zodiac;
                }

                public String getFestival() {
                    return festival;
                }

                public void setFestival(String festival) {
                    this.festival = festival;
                }

                public String getWeekday() {
                    return weekday;
                }

                public void setWeekday(String weekday) {
                    this.weekday = weekday;
                }

                public String getNlmonth() {
                    return nlmonth;
                }

                public void setNlmonth(String nlmonth) {
                    this.nlmonth = nlmonth;
                }

                public boolean isNegative() {
                    return negative;
                }

                public void setNegative(boolean negative) {
                    this.negative = negative;
                }

                public String getMonth() {
                    return month;
                }

                public void setMonth(String month) {
                    this.month = month;
                }

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public String getDay() {
                    return day;
                }

                public void setDay(String day) {
                    this.day = day;
                }

                public String getNlday() {
                    return nlday;
                }

                public void setNlday(String nlday) {
                    this.nlday = nlday;
                }

                public static class BeforeBean {
                    /**
                     * negative : true
                     * nlyear : 己亥
                     * month : 2
                     * year : 2019
                     * daysInterval : 247
                     * zodiac : 猪
                     * festival : 春节
                     * weekday : 星期二
                     * text : 2月5日
                     * day : 5
                     * nlmonth : 正月
                     * nlday : 初一
                     */

                    private boolean negative;
                    private String nlyear;
                    private String month;
                    private String year;
                    private int daysInterval;
                    private String zodiac;
                    private String festival;
                    private String weekday;
                    private String text;
                    private String day;
                    private String nlmonth;
                    private String nlday;

                    public boolean isNegative() {
                        return negative;
                    }

                    public void setNegative(boolean negative) {
                        this.negative = negative;
                    }

                    public String getNlyear() {
                        return nlyear;
                    }

                    public void setNlyear(String nlyear) {
                        this.nlyear = nlyear;
                    }

                    public String getMonth() {
                        return month;
                    }

                    public void setMonth(String month) {
                        this.month = month;
                    }

                    public String getYear() {
                        return year;
                    }

                    public void setYear(String year) {
                        this.year = year;
                    }

                    public int getDaysInterval() {
                        return daysInterval;
                    }

                    public void setDaysInterval(int daysInterval) {
                        this.daysInterval = daysInterval;
                    }

                    public String getZodiac() {
                        return zodiac;
                    }

                    public void setZodiac(String zodiac) {
                        this.zodiac = zodiac;
                    }

                    public String getFestival() {
                        return festival;
                    }

                    public void setFestival(String festival) {
                        this.festival = festival;
                    }

                    public String getWeekday() {
                        return weekday;
                    }

                    public void setWeekday(String weekday) {
                        this.weekday = weekday;
                    }

                    public String getText() {
                        return text;
                    }

                    public void setText(String text) {
                        this.text = text;
                    }

                    public String getDay() {
                        return day;
                    }

                    public void setDay(String day) {
                        this.day = day;
                    }

                    public String getNlmonth() {
                        return nlmonth;
                    }

                    public void setNlmonth(String nlmonth) {
                        this.nlmonth = nlmonth;
                    }

                    public String getNlday() {
                        return nlday;
                    }

                    public void setNlday(String nlday) {
                        this.nlday = nlday;
                    }
                }
            }
        }

        public static class SpeakBean {
            /**
             * text : 2020年春节是1月25日，距今还有107天
             * type : text
             */

            private String text;
            private String type;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        public static class CommandBean {
            /**
             * api :
             */

            private String api;

            public String getApi() {
                return api;
            }

            public void setApi(String api) {
                this.api = api;
            }
        }
    }


}
