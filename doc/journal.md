Weird Science / Zettalib devjournal
-----------------------------------

Previously...
=============
Before producing these notes, I:
* Re-implemented gas code. Internally the BlockGas has a few functions that convert from a block's metadata value to a "concentration" value, meant to be representative of parts per million or pressure or something like that.
	- Ticking is presently a huge hack: On creation, schedule an update. Every update, schedule an update. Admittedly dumb: I'll need to write something less awful later
	- As another hack, you can get a valid concentration value for any block - air is 0, anything else is (maxConcentration + 1). I actually like this hack since it's only used internally (and therefore wrapped from the rest of the project nicely) and it makes the gas flow code much more elegant.
* Wrote an @Conf and an @Configgable annotation, and a parser for them.
	- @Conf values are ones which, when parsed, are set via config file. Currently only works for primitive datatypes: int, boolean, double, and String. I doubt we'll need more with the exception of lists, and I'm not sure how best to implement those.
		+ Has a required "name" property, which will be the name of the setting in the config file.
		+ Has a technically optional "def" property, shorthand for default, of type String. The default defaults (ergh) to "0", which will probably not break anything but you will also probably want to specify a saner default.
		+ Has a "section" property, which determines its section in the config file.
	- @Configgables are classes which have @Conf values on their fields. Used for recursively doing config annotation parsing. Also can have a "section" property that overrides the one in the contained @Confs.
		+ In an ideal world, the section on a @Conf would instead put it in a nested sub-section of the @Configgable.
	- The parser requires context: It will just NPE unless you give it a net.minecraftforge.common.config.Configuration. This also determines which file we're working with, as a Configuration retains its filename (or probably the loaded data) as state.
	- All of this code requires much much more sanity checking, consider that a TODO
* A bunch of simple chemistry stuff which, hopefully, is sufficiently self-documenting.

Day 3
=====
Planning to write more init stuff. Let's try to derive a bunch of things that would otherwise take boilerplate code.
Initially wanted an INamed interface, but then that's totally unnecessary inheritance stuff so instead I'll just pass a name as an arg.

As of now, InitUtils has two variables of required state, String modid; and Logger log;, and two variables of optional state, Configuration config; and public CreativeTabs tab;.

Rethinking: Should do lowerCamelCase and no prepended "item" or "block".
