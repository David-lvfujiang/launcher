package com.fenda.calendar.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @Author: david.lvfujiang
 * @Date: 2019/10/10
 * @Describe: 查询星座
 */
public class QueryConstellationBean {

    /**
     * recordId : b7afea5c855a4103b0f76985c32cc657
     * skillId : 2019031800001161
     * requestId : b7afea5c855a4103b0f76985c32cc657
     * nlu : {"skillId":"2019031800001161","res":"5d8ae7040d32eb000d70f883","input":"今天出生的人是什么星座的","inittime":17.2119140625,"loadtime":20.137939453125,"skill":"日历","skillVersion":"38","source":"dui","systime":234.83203125,"semantics":{"request":{"slots":[{"pos":[0,1],"rawvalue":"今天","name":"阳历日期","rawpinyin":"jin tian","value":"20191010"},{"name":"intent","value":"查询星座"},{"name":"查询对象","value":"星座"}],"task":"日历","confidence":1,"slotcount":3,"rules":{"5d8ae7040d32eb000d70f9b9":{"查询对象":["星座",-1,-1]},"5d8ae7040d32eb000d70f9b3":{"阳历日期":["20191010",0,1],"intent":["查询星座",-1,-1]}}}},"version":"2019.1.15.20:40:58","timestamp":1570692280}
     * dm : {"input":"今天出生的人是什么星座的","shouldEndSession":true,"task":"日历","widget":{"widgetName":"default","duiWidget":"text","extra":{"constellation_end":"10月23日","constellation_begin":"9月23日","nlyear":"己亥","month":10,"constellation":"天秤座","year":2019,"weekday":"星期四","day":10,"nlmonth":"九月","nlday":"十二"},"name":"default","text":"10月10日","type":"text"},"intentName":"查询星座","runSequence":"nlgFirst","nlg":"今天出生的是天秤座","intentId":"5d8ae7040d32eb000d70f89b","speak":{"text":"今天出生的是天秤座","type":"text"},"command":{"api":""},"taskId":"5d8ae7040d32eb000d70f883","status":1}
     * contextId : 4828da037b3c4f62ab10bf932fbb42c5
     * sessionId : 4828da037b3c4f62ab10bf932fbb42c5
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
         * res : 5d8ae7040d32eb000d70f883
         * input : 今天出生的人是什么星座的
         * inittime : 17.2119140625
         * loadtime : 20.137939453125
         * skill : 日历
         * skillVersion : 38
         * source : dui
         * systime : 234.83203125
         * semantics : {"request":{"slots":[{"pos":[0,1],"rawvalue":"今天","name":"阳历日期","rawpinyin":"jin tian","value":"20191010"},{"name":"intent","value":"查询星座"},{"name":"查询对象","value":"星座"}],"task":"日历","confidence":1,"slotcount":3,"rules":{"5d8ae7040d32eb000d70f9b9":{"查询对象":["星座",-1,-1]},"5d8ae7040d32eb000d70f9b3":{"阳历日期":["20191010",0,1],"intent":["查询星座",-1,-1]}}}}
         * version : 2019.1.15.20:40:58
         * timestamp : 1570692280
         */

        private String skillId;
        private String res;
        private String input;
        private double inittime;
        private double loadtime;
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

        public double getInittime() {
            return inittime;
        }

        public void setInittime(double inittime) {
            this.inittime = inittime;
        }

        public double getLoadtime() {
            return loadtime;
        }

        public void setLoadtime(double loadtime) {
            this.loadtime = loadtime;
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
             * request : {"slots":[{"pos":[0,1],"rawvalue":"今天","name":"阳历日期","rawpinyin":"jin tian","value":"20191010"},{"name":"intent","value":"查询星座"},{"name":"查询对象","value":"星座"}],"task":"日历","confidence":1,"slotcount":3,"rules":{"5d8ae7040d32eb000d70f9b9":{"查询对象":["星座",-1,-1]},"5d8ae7040d32eb000d70f9b3":{"阳历日期":["20191010",0,1],"intent":["查询星座",-1,-1]}}}
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
                 * slots : [{"pos":[0,1],"rawvalue":"今天","name":"阳历日期","rawpinyin":"jin tian","value":"20191010"},{"name":"intent","value":"查询星座"},{"name":"查询对象","value":"星座"}]
                 * task : 日历
                 * confidence : 1
                 * slotcount : 3
                 * rules : {"5d8ae7040d32eb000d70f9b9":{"查询对象":["星座",-1,-1]},"5d8ae7040d32eb000d70f9b3":{"阳历日期":["20191010",0,1],"intent":["查询星座",-1,-1]}}
                 */

                private String task;
                private int confidence;
                private int slotcount;
                private RulesBean rules;
                private List<SlotsBean> slots;

                public String getTask() {
                    return task;
                }

                public void setTask(String task) {
                    this.task = task;
                }

                public int getConfidence() {
                    return confidence;
                }

                public void setConfidence(int confidence) {
                    this.confidence = confidence;
                }

                public int getSlotcount() {
                    return slotcount;
                }

                public void setSlotcount(int slotcount) {
                    this.slotcount = slotcount;
                }

                public RulesBean getRules() {
                    return rules;
                }

                public void setRules(RulesBean rules) {
                    this.rules = rules;
                }

                public List<SlotsBean> getSlots() {
                    return slots;
                }

                public void setSlots(List<SlotsBean> slots) {
                    this.slots = slots;
                }

                public static class RulesBean {
                    /**
                     * 5d8ae7040d32eb000d70f9b9 : {"查询对象":["星座",-1,-1]}
                     * 5d8ae7040d32eb000d70f9b3 : {"阳历日期":["20191010",0,1],"intent":["查询星座",-1,-1]}
                     */

                    @SerializedName("5d8ae7040d32eb000d70f9b9")
                    private _$5d8ae7040d32eb000d70f9b9Bean _$5d8ae7040d32eb000d70f9b9;
                    @SerializedName("5d8ae7040d32eb000d70f9b3")
                    private _$5d8ae7040d32eb000d70f9b3Bean _$5d8ae7040d32eb000d70f9b3;

                    public _$5d8ae7040d32eb000d70f9b9Bean get_$5d8ae7040d32eb000d70f9b9() {
                        return _$5d8ae7040d32eb000d70f9b9;
                    }

                    public void set_$5d8ae7040d32eb000d70f9b9(_$5d8ae7040d32eb000d70f9b9Bean _$5d8ae7040d32eb000d70f9b9) {
                        this._$5d8ae7040d32eb000d70f9b9 = _$5d8ae7040d32eb000d70f9b9;
                    }

                    public _$5d8ae7040d32eb000d70f9b3Bean get_$5d8ae7040d32eb000d70f9b3() {
                        return _$5d8ae7040d32eb000d70f9b3;
                    }

                    public void set_$5d8ae7040d32eb000d70f9b3(_$5d8ae7040d32eb000d70f9b3Bean _$5d8ae7040d32eb000d70f9b3) {
                        this._$5d8ae7040d32eb000d70f9b3 = _$5d8ae7040d32eb000d70f9b3;
                    }

                    public static class _$5d8ae7040d32eb000d70f9b9Bean {
                        private List<String> 查询对象;

                        public List<String> get查询对象() {
                            return 查询对象;
                        }

                        public void set查询对象(List<String> 查询对象) {
                            this.查询对象 = 查询对象;
                        }
                    }

                    public static class _$5d8ae7040d32eb000d70f9b3Bean {
                        private List<String> 阳历日期;
                        private List<String> intent;

                        public List<String> get阳历日期() {
                            return 阳历日期;
                        }

                        public void set阳历日期(List<String> 阳历日期) {
                            this.阳历日期 = 阳历日期;
                        }

                        public List<String> getIntent() {
                            return intent;
                        }

                        public void setIntent(List<String> intent) {
                            this.intent = intent;
                        }
                    }
                }

                public static class SlotsBean {
                    /**
                     * pos : [0,1]
                     * rawvalue : 今天
                     * name : 阳历日期
                     * rawpinyin : jin tian
                     * value : 20191010
                     */

                    private String rawvalue;
                    private String name;
                    private String rawpinyin;
                    private String value;
                    private List<Integer> pos;

                    public String getRawvalue() {
                        return rawvalue;
                    }

                    public void setRawvalue(String rawvalue) {
                        this.rawvalue = rawvalue;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getRawpinyin() {
                        return rawpinyin;
                    }

                    public void setRawpinyin(String rawpinyin) {
                        this.rawpinyin = rawpinyin;
                    }

                    public String getValue() {
                        return value;
                    }

                    public void setValue(String value) {
                        this.value = value;
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
         * input : 今天出生的人是什么星座的
         * shouldEndSession : true
         * task : 日历
         * widget : {"widgetName":"default","duiWidget":"text","extra":{"constellation_end":"10月23日","constellation_begin":"9月23日","nlyear":"己亥","month":10,"constellation":"天秤座","year":2019,"weekday":"星期四","day":10,"nlmonth":"九月","nlday":"十二"},"name":"default","text":"10月10日","type":"text"}
         * intentName : 查询星座
         * runSequence : nlgFirst
         * nlg : 今天出生的是天秤座
         * intentId : 5d8ae7040d32eb000d70f89b
         * speak : {"text":"今天出生的是天秤座","type":"text"}
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
             * extra : {"constellation_end":"10月23日","constellation_begin":"9月23日","nlyear":"己亥","month":10,"constellation":"天秤座","year":2019,"weekday":"星期四","day":10,"nlmonth":"九月","nlday":"十二"}
             * name : default
             * text : 10月10日
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
                 * constellation_end : 10月23日
                 * constellation_begin : 9月23日
                 * nlyear : 己亥
                 * month : 10
                 * constellation : 天秤座
                 * year : 2019
                 * weekday : 星期四
                 * day : 10
                 * nlmonth : 九月
                 * nlday : 十二
                 */

                private String constellation_end;
                private String constellation_begin;
                private String nlyear;
                private int month;
                private String constellation;
                private int year;
                private String weekday;
                private int day;
                private String nlmonth;
                private String nlday;

                public String getConstellation_end() {
                    return constellation_end;
                }

                public void setConstellation_end(String constellation_end) {
                    this.constellation_end = constellation_end;
                }

                public String getConstellation_begin() {
                    return constellation_begin;
                }

                public void setConstellation_begin(String constellation_begin) {
                    this.constellation_begin = constellation_begin;
                }

                public String getNlyear() {
                    return nlyear;
                }

                public void setNlyear(String nlyear) {
                    this.nlyear = nlyear;
                }

                public int getMonth() {
                    return month;
                }

                public void setMonth(int month) {
                    this.month = month;
                }

                public String getConstellation() {
                    return constellation;
                }

                public void setConstellation(String constellation) {
                    this.constellation = constellation;
                }

                public int getYear() {
                    return year;
                }

                public void setYear(int year) {
                    this.year = year;
                }

                public String getWeekday() {
                    return weekday;
                }

                public void setWeekday(String weekday) {
                    this.weekday = weekday;
                }

                public int getDay() {
                    return day;
                }

                public void setDay(int day) {
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

        public static class SpeakBean {
            /**
             * text : 今天出生的是天秤座
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
